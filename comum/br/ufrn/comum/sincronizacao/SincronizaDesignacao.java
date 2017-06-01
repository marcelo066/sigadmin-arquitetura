/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 24/02/2010
 */
package br.ufrn.comum.sincronizacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import br.ufrn.arq.dao.Database;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.arq.util.Formatador;

/**
 * 
 * Classe para realizar a sincroniza��o dos dados de designa��es dos servidores
 * da base de dados do SIGRH para o SIGAA.
 * 
 * @author Raphaela Galhardo
 * @author Gleydson Lima
 *
 */
public class SincronizaDesignacao {

	public static void sincronizar(Connection sigaaCon, Connection sipacCon)
			throws Exception {


		//Data da ultima atualizacao de designacoes no SIGAA
		java.util.Date dataUltimaAtualizacao = null;
		PreparedStatement pStUltimaAtualizacao = sigaaCon.prepareStatement("select max(ultima_atualizacao) from rh.designacao");
		ResultSet rsUltimaAtualizacao = pStUltimaAtualizacao.executeQuery();
		if (rsUltimaAtualizacao.next()) {
			dataUltimaAtualizacao = rsUltimaAtualizacao.getDate(1);
		}
		System.out.println("�LTIMA ATUALIZA��O NO SIGAA DAS DESIGNACOES: " + Formatador.getInstance().formatarData(dataUltimaAtualizacao));
			
		
		if (dataUltimaAtualizacao == null ){
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, 6);
			cal.set(Calendar.YEAR, 2007);
			
			dataUltimaAtualizacao = cal.getTime();
		}
	
		/*
		 * Somente migra dados das designacoes com algum dado alterado ap�s a �ltima atualiza��o da base de dados
		 * vinda do SIGRH para o SIGAA.
		 */

		
		// consulta designacoes na base de dados do SIGRH com altera��es ap�s a data da �ltima atualiza��o no SIGAA
		String sqlConsultaDesignacoesSIGRH = 
				"select * FROM  rh.designacao d where (d.ultima_atualizacao >= ?  or d.data_cadastro >= ?) and d.id_unidade is not null ";
		PreparedStatement pStConsultaServidoresSIGRH = sipacCon.prepareStatement(sqlConsultaDesignacoesSIGRH);
		pStConsultaServidoresSIGRH.setDate(1, new java.sql.Date(dataUltimaAtualizacao.getTime()));
		pStConsultaServidoresSIGRH.setDate(2, new java.sql.Date(dataUltimaAtualizacao.getTime()));
		ResultSet rsDesignacoesSipac = pStConsultaServidoresSIGRH.executeQuery();
		
		PreparedStatement pStInsertSigaa = sigaaCon
				.prepareStatement("INSERT INTO rh.Designacao(ID_DESIGNACAO, ID_SERVIDOR, ID_ATIVIDADE, " +
								  "INICIO, FIM, ORIGEM, UORG, NOME_UNIDADE, ID_UNIDADE, GERENCIA, " +
								  "REMUNERACAO, DATA_DOCUMENTO, ULTIMA_ATUALIZACAO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,now())") ;

		String updateSql = "UPDATE rh.Designacao SET ID_SERVIDOR = ?, ID_ATIVIDADE = ?, INICIO = ?, FIM = ?, ORIGEM = ?, " +
								"UORG=?, NOME_UNIDADE = ?, ID_UNIDADE = ?, GERENCIA = ?, " +
								"REMUNERACAO = ?, DATA_DOCUMENTO = ?, ultima_atualizacao = now() " +
								"WHERE ID_DESIGNACAO = ?";

		PreparedStatement pStUpdateSigaa = sigaaCon.prepareStatement(updateSql);
		PreparedStatement pStSelectSigaa = sigaaCon.prepareStatement("select * from rh.designacao where id_designacao = ?");

		int inseridos = 0;
		int atualizados = 0;
		int erros = 0;

		while (rsDesignacoesSipac.next()) {
			
			//Varrendo designacoes no SIGRH
			
			//Identificador da designacao sigaa = sigrh
			int idDesignacao = rsDesignacoesSipac.getInt("ID_DESIGNACAO");
			
			//Identificador da atividade no sigaa = sigrh
			int idAtividadeSipac = rsDesignacoesSipac.getInt("ID_ATIVIDADE");
										
			//Identificador do servidor no sigaa
			int idServidorSipac = RhUtil.getIdServidor(rsDesignacoesSipac.getInt("ID_SERVIDOR"), sigaaCon, sipacCon);
			
			pStSelectSigaa.setInt(1, idDesignacao);
			ResultSet rsSelectSigaa = pStSelectSigaa.executeQuery();
			
			if (idServidorSipac > 0 && !rsSelectSigaa.next()){
				
				//Ainda NAO cadastrada no SIGAA
				int i = 0;
				
				pStInsertSigaa.setInt(++i, idDesignacao);
				pStInsertSigaa.setInt(++i, idServidorSipac);
				pStInsertSigaa.setInt(++i, idAtividadeSipac);
				pStInsertSigaa.setDate(++i, rsDesignacoesSipac.getDate("INICIO"));
				pStInsertSigaa.setDate(++i, rsDesignacoesSipac.getDate("FIM"));
				pStInsertSigaa.setString(++i, rsDesignacoesSipac.getString("ORIGEM"));
				pStInsertSigaa.setString(++i, rsDesignacoesSipac.getString("UORG"));
				pStInsertSigaa.setString(++i, rsDesignacoesSipac.getString("NOME_UNIDADE"));
				pStInsertSigaa.setInt(++i, rsDesignacoesSipac.getInt("ID_UNIDADE"));					
				pStInsertSigaa.setString(++i, rsDesignacoesSipac.getString("GERENCIA"));
				pStInsertSigaa.setBoolean(++i, rsDesignacoesSipac.getBoolean("REMUNERACAO"));
				pStInsertSigaa.setDate(++i, rsDesignacoesSipac.getDate("DATA_DOCUMENTO"));
				
				try {
					pStInsertSigaa.executeUpdate();
					inseridos++;
				} catch (Exception e) {
					if (!e.getMessage().startsWith("ERROR: insert or update on table") && !e.getMessage().startsWith("ERROR: duplicate key")) {
						System.err.println(e.getMessage());
					}
					erros++;
				}
				pStInsertSigaa.clearParameters();
				
			}else{
				
				//Dados no sipac
				Date dataInicioSipac = rsDesignacoesSipac.getDate("inicio");
				Date dataFimSipac = rsDesignacoesSipac.getDate("FIM");
				int idUnidadeSipac = rsDesignacoesSipac.getInt("ID_UNIDADE"); 
				
				//Ja cadastrada no SIGAA, atualiza
				int idServidorSigaa = rsSelectSigaa.getInt("id_servidor");
				int idAtividadeSigaa = rsSelectSigaa.getInt("id_atividade");
				Date dataInicioSigaa = rsSelectSigaa.getDate("inicio");
				Date dataFimSigaa = rsSelectSigaa.getDate("FIM");
				int idUnidadeSigaa = rsSelectSigaa.getInt("ID_UNIDADE"); 
				
				
				if (	idServidorSipac != idServidorSigaa || idAtividadeSipac != idAtividadeSigaa || 
						( dataInicioSipac == null && dataInicioSigaa != null) || (dataInicioSigaa == null && dataInicioSipac != null) || 
						( dataInicioSipac != null && dataInicioSigaa != null && !dataInicioSipac.equals(dataInicioSigaa)) || 
						( dataFimSipac == null && dataFimSigaa != null) || (dataFimSigaa == null && dataFimSipac != null) || 
						( dataFimSipac != null && dataFimSigaa != null && !dataFimSipac.equals(dataFimSigaa)) ||
						idUnidadeSipac != idUnidadeSigaa){
				
					
					int i = 0;				
					pStUpdateSigaa.setInt(++i, idServidorSipac);
					pStUpdateSigaa.setInt(++i, idAtividadeSipac);
					pStUpdateSigaa.setDate(++i, dataInicioSipac);
					pStUpdateSigaa.setDate(++i, dataFimSipac);
					pStUpdateSigaa.setString(++i, rsDesignacoesSipac.getString("ORIGEM"));
					pStUpdateSigaa.setString(++i, rsDesignacoesSipac.getString("UORG"));
					pStUpdateSigaa.setString(++i, rsDesignacoesSipac.getString("NOME_UNIDADE"));
					pStUpdateSigaa.setInt(++i, idUnidadeSipac);				
					pStUpdateSigaa.setString(++i, rsDesignacoesSipac.getString("GERENCIA"));
					pStUpdateSigaa.setBoolean(++i, rsDesignacoesSipac.getBoolean("REMUNERACAO"));
					pStUpdateSigaa.setDate(++i, rsDesignacoesSipac.getDate("DATA_DOCUMENTO"));
					
					pStUpdateSigaa.setInt(++i, idDesignacao);
	
					try {
						pStUpdateSigaa.executeUpdate();
						pStUpdateSigaa.clearParameters();
						atualizados++;
					} catch (Exception e) {
						if (!e.getMessage().startsWith("ERROR: insert or update on table") && !e.getMessage().startsWith("ERROR: duplicate key")) {
							System.err.println(e.getMessage());
						}
						erros++;
					}
				}
				
			}
			
		}
		System.out.println("Total INSERIDOS: " + inseridos);
		System.out.println("Total ATUALIZADOS: " + atualizados);
		System.out.println("Total ERRO: " + erros);
	}
	
	
	/**
	 * M�todo para remover designa��es nos bancos acad�mico e sistemas comum.
	 * @param con
	 * @param idDesignacao
	 * @throws Exception
	 */
	public void removerDesignacao(Connection con, int idDesignacao) throws Exception {
		try {
			PreparedStatement st = con.prepareStatement("delete from rh.designacao " + "where id_designacao=?");
			st.setInt(1, idDesignacao);
			st.executeUpdate();
		} catch(Exception e) {
			throw new DAOException("Erro na sincroniza��o com os bancos de dados SIPAC/SIGAA", e);
		} finally {
			con.close();
		}

	}

	public static void main(String[] args) throws Exception {
		sincronizar(Database.getInstance().getSigaaConnection(),Database.getInstance().getSipacConnection());
	}


}
