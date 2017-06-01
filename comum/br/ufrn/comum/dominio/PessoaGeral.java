/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 15/09/2004
 */
package br.ufrn.comum.dominio;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.util.Date;
import java.util.StringTokenizer;

import javax.persistence.Transient;

import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.util.CalendarUtils;
import br.ufrn.arq.util.Formatador;
import br.ufrn.arq.util.HashCodeUtil;
import br.ufrn.arq.util.StringUtils;
import br.ufrn.arq.util.ValidatorUtil;
import br.ufrn.rh.dominio.Formacao;

/**
 * Classe de dom�nio das Pessoas dos sistemas.
 *
 * @author Gleydson Lima
 *
 */
public class PessoaGeral implements PersistDB, Cloneable {

	/** Texto a ser exibido no campo CPF quando a pessoa for extrangeira. */
	private static final String ESTRANGEIRO = "ESTRANGEIRO";
	
	/** Constante utilizada para Serializa��o */
	private static final long serialVersionUID = -2863325081475888817L;
	
	/**
	 * Constante para tipo de pessoa Pessoa F�sica
	 * @see #tipo
	 */
	public static final char PESSOA_FISICA = 'F';

	/**
	 * Constante para tipo de pessoa Pessoa Jur�dica
	 * @see #tipo
	 */
	public static final char PESSOA_JURIDICA = 'J';
	
	/**
	 * Constante que indica que a pessoa � do tipo Unidade Gestora
	 */
	public static final char UNIDADE_GESTORA = 'G';
	
	/**
	 * Constante para atribui��o do sexo feminino
	 */
	public static final char SEXO_FEMININO = 'F';
	
	/**
	 * Constante para atribui��o do sexo masculino
	 */
	public static final char SEXO_MASCULINO = 'M';
	
	
	/** Identificador */
	protected int id;

	/** CPF ou CNPJ da pessoa */
	protected Long cpf_cnpj;

	/** Nome da pessoa ou raz�o social em caso de empresa */
	protected String nome;
	
	/** Nome da pessoa ou raz�o social em caso de empresa em ASCII, para auxiliar consultas */
	protected String nomeAscii;

	/** Em caso de pessoa jur�dica, nome fantasia da empresa */
	protected String nomeFantasia;

	/** Em caso de pessoa jur�dica, nome do representante da empresa */
	protected String nomeRepresentante;

	/** Endere�o da pessoa/empresa */
	protected String endereco;

	/** Bairro da pessoa/empresa */
	protected String bairro;

	/** CEP da pessoa/empresa */
	protected String CEP;

	/** Cidade da pessoa/empresa */
	protected String cidade;

	/** Unidade Federativa da pessoa/empresa */
	protected String UF;

	/** C�digo da �rea nacional de telefone fixo da pessoa */
	protected Short codigoAreaNacionalTelefoneFixo;

	/** C�digo da �rea nacional de telefone celular da pessoa */
	protected Short codigoAreaNacionalTelefoneCelular;

	/** Telefone da pessoa/empresa */
	protected String telefone;

	/** FAX da pessoa */
	protected String fax;

	/** Celular da pessoa */
	protected String celular;

	/** NIT/PIS da pessoa f�sica */
	protected Long nitPIS;

	/** Tipo de pessoa. F - F�sica; J - Jur�dica. */
	protected char tipo;

	/** Data de nascimento da pessoa f�sica */
	protected Date dataNascimento;

	/** Sexo da pessoa. M - Masculino; F - Feminino. */
	protected char sexo;

	/** E-Mail da pessoa/empresa */
	protected String email;
	
	/** Identidade: Registro Geral */
	protected String registroGeral;
	
	/** �rg�o expedidor do RG (identidade) da pessoa*/
	protected String rgOrgaoExpedidor;
	
	/** Data da expedi��o do RG */
	protected Date rgDataExpedicao;
	
	/** Complemento do endere�o residencial da pessoa */
	protected String enderecoComplemento;
	
	/** N�mero da casa/apto associado ao logradouro */
	protected String enderecoNumero;

	/** Em caso de pessoa f�sica, nome da m�e da pessoa. */
	protected String nomeMae;

	/** Em caso de pessoa f�sica, nome do pai da pessoa. */
	protected String nomePai;

	/** Informa se a pessoa tem CPF ou CNPJ v�lido.. usado para restringir a busca de pessoas migradas erradas */
	protected boolean valido;

	/**
	 * Para pessoa jur�dica. Atributo utilizado pelo subsistema de faturas para indicar quando um
	 * fornecedor pode ou n�o emitir uma fatura.
	 */
	protected boolean emiteFatura = false;
	
	/**
	 * Para pessoa jur�dica. Al�quota de imposto a ser descontado no valor bruto das faturas.
	 */
	protected Double aliquotaImposto = null;

	/**
	 * Atributo utilizado pelo subsistema de conv�nios para indicar quando um
	 * fornecedor � internacional e n�o possui cnpj.
	 */
	protected boolean internacional = false;
	

	/** N�mero do passaporte, para o caso de estrangeiros */
	protected String passaporte;

	/** Pais de origem de uma pessoa estrangeira* */
	protected String paisOrigem;

	/** Unidade em que est� lotado * */
	protected UnidadeGeral lotacao;

	private Integer idPerfil;
	
	private Formacao formacao;
	
	/*
	 * Atributos utilizados no cadastro de algumas requisi��es,
	 * onde se exige dados de um proposto.
	 */
	
	/** Informa��o utilizada Matricula do proposto * */
	protected String matricula;

	/** Se a pessoa � funcion�ria da universidade */
	protected Boolean funcionario;

	
	/** Construtores */
	
	public PessoaGeral() {
		sexo = 'N';
		tipo = PESSOA_FISICA;
		lotacao = new UnidadeGeral();
	}

	public PessoaGeral(int id) {
		this.id = id;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PessoaGeral f = (PessoaGeral) super.clone();
		return f;
	}

	@Override
	public boolean equals(Object other) {
		if (other != null && other instanceof PessoaGeral) {
			PessoaGeral fornecedor = (PessoaGeral) other;
			if (id == fornecedor.getId()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, cpf_cnpj);
		return result;
	}
	
	
	/** M�todo para recuperar o Bairro */
	public String getBairro() {
		return bairro;
	}

	/** M�todo para setar o Bairro */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/** M�todo para recuperar o celular */
	public String getCelular() {
		return celular;
	}

	/** M�todo para setar o celular */
	public void setCelular(String celular) {
		this.celular = celular;
	}

	/** M�todo para recuperar o CEP */
	public String getCEP() {
		return CEP;
	}

	/** M�todo para setar o CEP */
	public void setCEP(String cep) {
		CEP = cep;
	}

	/** M�todo para recuperar a Cidade */
	public String getCidade() {
		return cidade;
	}

	/** M�todo para setar Cidade */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/** M�todo para recuperar cpf/cnpj */
	public Long getCpf_cnpj() {
		return cpf_cnpj;
	}

	/** M�todo para setar cpf/cnpj */
	public void setCpf_cnpj(Long cpf_cnpj) {
		this.cpf_cnpj = cpf_cnpj;
	}

	/** M�todo para setar cpf/cnpj como uma string */
	public void setCpf_cnpjString(String cpf_cnpj) {
		if (cpf_cnpj == null || cpf_cnpj.trim().isEmpty()) {
			this.cpf_cnpj = null;
		} else  if (cpf_cnpj.equalsIgnoreCase(ESTRANGEIRO)) {
			this.cpf_cnpj = null;
		} else {
			try {
				this.cpf_cnpj = Long.parseLong(cpf_cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", ""));
			} catch(NumberFormatException e) {
				this.cpf_cnpj = null;
			}
		}
	}

	/** M�todo para recuperar cpf/cnpj como string */
	public String getCpf_cnpjString() {
		String cpfCnpj = "";
		if(isInternacional())
			cpfCnpj = ESTRANGEIRO;
		else
			cpfCnpj = getCpfCnpjFormatado();
		
		return cpfCnpj;
	}

	/** M�todo para recuperar o endere�o*/
	public String getEndereco() {
		return endereco;
	}

	/** M�todo para setar o endere�o*/
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	/** M�todo para recuperar o fax*/
	public String getFax() {
		return fax;
	}

	/** M�todo para setar o fax*/
	public void setFax(String fax) {
		this.fax = fax;
	}

	/** M�todo para recuperar o id*/
	public int getId() {
		return id;
	}

	/** M�todo para setar o id*/
	public void setId(int id) {
		this.id = id;
	}
	
	/** M�todo para recuperar o nome representante*/
	public String getNomeRepresentante() {
		return nomeRepresentante;
	}

	/** M�todo para setar o nome representante*/
	public void setNomeRepresentante(String nomeRepresentante) {
		this.nomeRepresentante = nomeRepresentante;
	}

	/** M�todo de acesso do nome */
	public String getNome() {
		if(!isEmpty(nome)){
			return nome.trim();
		}
		return nome;
	}
	
	/** M�todo de acesso do nome e cpf/cnpj */
	public String getNomeCpfCnpj() {
		String cpfCnpj;
		if(getTipo() == 'G')
			cpfCnpj = getCpf_cnpjString();
		else if(isInternacional())
			cpfCnpj = ESTRANGEIRO;
		else
			cpfCnpj = getCpfCnpjFormatado();
		return getNome() + "(" + cpfCnpj + ")";
	}

	/** M�todo para setar o nome */
	public void setNome(String nome) {
		this.nome = nome;
		setNomeAscii(nome);
	}

	/** M�todo para recuperar o telefone*/
	public String getTelefone() {
		return telefone;
	}

	/** M�todo para setar o telefone*/
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/** M�todo para recuperar o tipo da pessoa*/
	public char getTipo() {
		return tipo;
	}

	/** M�todo para setar o tipo da pessoa*/
	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	/** M�todo para recuperar a UF*/
	public String getUF() {
		return UF;
	}

	/** M�todo para setar a UF*/
	public void setUF(String uf) {
		UF = uf;
	}

	/** M�todo para recuperar o NitPIS */
	public Long getNitPIS() {
		return nitPIS;
	}

	/** M�todo para setar o NitPIS */
	public void setNitPIS(Long nitPIS) {
		this.nitPIS = nitPIS;
	}

	/** M�todo para recuperar o matr�cula */
	public String getMatricula() {
		return matricula;
	}

	/** M�todo para setar o matr�cula */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/** M�todo para recuperar o nome fantasia */
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	/** M�todo para setar o nome fantasia */
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	/** M�todo para recuperar a data de nascimento */
	public Date getDataNascimento() {
		return dataNascimento;
	}

	/** M�todo para setar a data de nascimento */
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/** M�todo para recuperar o sexo */
	public char getSexo() {
		return sexo;
	}

	/** M�todo para setar o sexo */
	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	/** M�todo para recuperar o email */
	public String getEmail() {
		return email;
	}

	/** M�todo para setar o email */
	public void setEmail(String email) {
		this.email = email;
	}

	/** M�todo para recuperar a raz�o social */
	public String getRazaoSocial() {
		return nome;
	}

	/** M�todo para verificar se a pessoa � um funcion�rio */
	public Boolean isFuncionario() {
		return funcionario;
	}

	/** M�todo para setar  a pessoa como um funcion�rio */
	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	/** M�todo para recuperar o cpf/cnpj formatado */
	public String getCpfCnpjFormatado() {
		if (getCpf_cnpj() == null){
			cpf_cnpj = new Long(0);
		}
		
		if (tipo == 'J')
			return Formatador.getInstance().formatarCNPJ(getCpf_cnpj());
		else if(tipo == 'F')
			return Formatador.getInstance().formatarCPF_CNPJ(getCpf_cnpj());
		else
			return String.valueOf(getCpf_cnpj());
	}

	/** M�todo para verificar se a pessoa pode emitir uma fatura*/
	public boolean isEmiteFatura() {
		return emiteFatura;
	}

	/** M�todo para setar a pessoa para emitir uma fatura*/
	public void setEmiteFatura(boolean emiteFatura) {
		this.emiteFatura = emiteFatura;
	}

	/** M�todo para verificar se a pessoa � estrangeira*/
	public boolean isInternacional() {
		return internacional;
	}

	/** M�todo para setar a pessoa como estrangeira*/
	public void setInternacional(boolean internacional) {
		this.internacional = internacional;
	}

	/** M�todo para recuperar a aliquota de imposto*/
	public Double getAliquotaImposto() {
		return aliquotaImposto;
	}

	/** M�todo para recuperar a aliquota de imposto*/
	public void setAliquotaImposto(Double aliquotaImposto) {
		this.aliquotaImposto = aliquotaImposto;
	}

	/**
	 * Retorna representa��o XML do Pessoa. Usado em consultas AJAX.
	 *
	 * @return representa��o XML da Pessoa.
	 */
	public String toXML() {
		String cpf_cnpjFormatado = Formatador.getInstance().formatarCPF_CNPJ(
				getCpf_cnpj() == null ? 0 : getCpf_cnpj());
		String dataFmt = null;
		if (dataNascimento != null)
			dataFmt = Formatador.getInstance().formatarData(dataNascimento);

		StringBuilder s = new StringBuilder(500);
		s.append("<PESSOA>\n");
		s.append("<ID>" + id + "</ID>\n");
		s.append("<NOME>" + (nome != null ? nome.trim() : null) + "</NOME>\n");
		s.append("<CPF_CNPJ>" + cpf_cnpjFormatado + "</CPF_CNPJ>\n");
		s.append("<TIPO>" + (tipo == '\0' ? ' ' : tipo) + "</TIPO>\n");
		s.append("<SEXO>" + ((sexo == '\0') ? 'M' : sexo) + "</SEXO>\n");
		s.append("<ENDERECO>"
				+ ((endereco == null || endereco.length() == 0) ? null
						: endereco.trim()) + "</ENDERECO>\n");
		s.append("<BAIRRO>" + (bairro != null ? bairro.trim() : null)
				+ "</BAIRRO>\n");
		s.append("<CEP>" + (CEP != null ? CEP.trim() : null) + "</CEP>\n");
		s.append("<CIDADE>"
				+ (cidade != null && cidade.length() > 0 ? cidade.trim()
						: cidade) + "</CIDADE>\n");
		s.append("<UF>" + (UF != null && UF.length() > 0 ? UF.trim() : null)
				+ "</UF>\n");
		s.append("<TELEFONE>"
				+ ((telefone != null && telefone.length() > 0) ? telefone
						.trim() : null) + "</TELEFONE>\n");
		s.append("<CELULAR>"
				+ ((celular != null && celular.length() > 0) ? celular.trim()
						: null) + "</CELULAR>\n");
		s.append("<FAX>"
				+ ((fax != null && fax.length() > 0) ? fax.trim() : null)
				+ "</FAX>\n");
		s.append("<EMAIL>"
				+ ((email != null && email.length() > 0) ? email.trim() : null)
				+ "</EMAIL>\n");
		s.append("<DATA_NASCIMENTO>" + dataFmt + "</DATA_NASCIMENTO>\n");
		s.append("</PESSOA>");

		return s.toString();
	}

	/** M�todo para recuperar o passaporte*/
	public String getPassaporte() {
		return passaporte;
	}

	/** M�todo para setar o passaporte*/
	public void setPassaporte(String passaporte) {
		this.passaporte = passaporte;
	}

	/** M�todo para recuperar o pais de origem*/
	public String getPaisOrigem() {
		return paisOrigem;
	}

	/** M�todo para setar o pais de origem*/
	public void setPaisOrigem(String paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	/** M�todo que retorna a unidade em que a pessoa est� locada*/
	public UnidadeGeral getLotacao() {
		return lotacao;
	}

	/** M�todo que setar uma unidade a pessoa */
	public void setLotacao(UnidadeGeral lotacao) {
		this.lotacao = lotacao;
	}

	/** M�todo que retorna se um cpf/cnpj est� v�lido*/
	public boolean isValido() {
		return valido;
	}

	/** M�todo para setar que a pessoa tem um cpf/cnpj est� v�lido*/
	public void setValido(boolean valido) {
		this.valido = valido;
	}

	/**M�todo para recuperar o c�digo de �rea do telefone fixo*/
	public Short getCodigoAreaNacionalTelefoneFixo() {
		return codigoAreaNacionalTelefoneFixo;
	}

	/**M�todo para setar o c�digo de �rea do telefone fixo*/
	public void setCodigoAreaNacionalTelefoneFixo(
			Short codigoAreaNacionalTelefoneFixo) {
		this.codigoAreaNacionalTelefoneFixo = codigoAreaNacionalTelefoneFixo;
	}

	/**M�todo para recuperar o c�digo de �rea do telefone celular*/
	public Short getCodigoAreaNacionalTelefoneCelular() {
		return codigoAreaNacionalTelefoneCelular;
	}

	/**M�todo para setar o c�digo de �rea do telefone celular*/
	public void setCodigoAreaNacionalTelefoneCelular(
			Short codigoAreaNacionalTelefoneCelular) {
		this.codigoAreaNacionalTelefoneCelular = codigoAreaNacionalTelefoneCelular;
	}

	/**M�todo para recuperar o idPerfil*/
	public Integer getIdPerfil() {
		return idPerfil;
	}

	/**M�todo para setar o idPerfil*/
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}
	
	/**M�todo para recuperar o NomeAscii*/
	public String getNomeAscii() {
		return nomeAscii;
	}

	/**M�todo para setar o NomeAscii*/
	public void setNomeAscii(String nomeAscii) {
		if (nomeAscii != null)
			this.nomeAscii = StringUtils.toAscii(nomeAscii);
	}
	
	/**M�todo para recuperar o registro geral*/
	public String getRegistroGeral() {
		return registroGeral;
	}

	/**M�todo para setar o registro geral*/
	public void setRegistroGeral(String registroGeral) {
		this.registroGeral = registroGeral;
	}

	/**M�todo para recuperar o endere�o completo*/
	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}

	/**M�todo para setar o endere�o completo*/
	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}
	
	/**M�todo para recuperar o endere�o/n�mero */
	public String getEnderecoNumero() {
		return enderecoNumero;
	}
	
	/**M�todo para setar o endere�o/n�mero*/
	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}

	/**M�todo para recuperar o nome da m�e*/
	public String getNomeMae() {
		return nomeMae;
	}

	/**M�todo para setar o nome da m�e*/
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	
	/**M�todo para recuperar o nome do pai*/
	public String getNomePai() {
		return nomePai;
	}
	
	/**M�todo para setar o nome do pai*/
	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	/**M�todo para recuperar o org�o expedidor do resgistro nacional da pessoa*/
	public String getRgOrgaoExpedidor() {
		return rgOrgaoExpedidor;
	}

	/**M�todo para setar o org�o expedidor do resgistro nacional da pessoa*/
	public void setRgOrgaoExpedidor(String rgOrgaoExpedidor) {
		this.rgOrgaoExpedidor = rgOrgaoExpedidor;
	}

	/**M�todo para recuperar a data de expedi��o do RG*/
	public Date getRgDataExpedicao() {
		return rgDataExpedicao;
	}

	/**M�todo para setar a data de expedi��o do RG*/
	public void setRgDataExpedicao(Date rgDataExpedicao) {
		this.rgDataExpedicao = rgDataExpedicao;
	}

	/**M�todo para verificar se a pessoa � do sexo feminino*/
	public boolean isSexoFeminino() {
		return (!ValidatorUtil.isEmpty(this.sexo) && this.sexo == SEXO_FEMININO);
	}

	/**M�todo para verificar se a pessoa � do sexo masculino*/
	public boolean isSexoMasculino() {
		return (!ValidatorUtil.isEmpty(this.sexo) && this.sexo == SEXO_MASCULINO);
	}
	
	/**M�todo para recuperar a forma��o da pessoa*/
	public Formacao getFormacao() {
		return formacao;
	}

	/**M�todo para setar a forma��o da pessoa*/
	public void setFormacao(Formacao formacao) {
		this.formacao = formacao;
	}

	/**
	 * Retorna a idade da pessoa na data atual.
	 * @return
	 */
	@Transient
	public int getIdade() {
		return CalendarUtils.calculoAnos(dataNascimento, new Date());
	}
	
	/**
	 * Retorna o nome abreviado da pessoa.
	 * @return
	 */
	@Transient
	public String getNomeAbreviado() {
		if (nome != null) {
			StringTokenizer nomeTok = new StringTokenizer(nome.trim());
			int count = nomeTok.countTokens();
			int count1 = nomeTok.countTokens() - 1;
			String nomeAbreviado = nomeTok.nextToken() + (nomeTok.hasMoreTokens() ? " " + nomeTok.nextToken() : "");
			for (int i = 2; i < count; i++){
				String aux = nomeTok.nextToken();
				if(aux.equals("DE") || aux.equals("DA") || aux.equals("DO") )
					nomeAbreviado += " " + aux;
				else
					if(i != count1 ){
						nomeAbreviado += " " + aux.charAt(0);
						nomeAbreviado += ".";
					}
					else
						nomeAbreviado += " " + aux;
			}
			return nomeAbreviado;
		}else
			return "";
	}
	
	/**
	 * Retorna os primeiros nomes da pessoa.
	 * @return
	 */
	@Transient
	public String getPrimeirosNomes() {
		if (getNome() == null) return "";
		String[] nomes = getNome().trim().split(" ");
		return String.format("%s %s %s", nomes[0], nomes[1], nomes[1].matches("(DE)|(DO)|(DA)") ? nomes[2] : "");
	}
}
