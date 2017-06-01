/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 08/07/2009
 */
package br.ufrn.arq.util;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import br.ufrn.arq.dao.DAOFactory;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.comum.dao.FeriadoDao;
import br.ufrn.comum.dominio.Feriado;

/**
 * Classe que implementa opera��es comuns sobre datas, tais como:
 * <ul>
 * <li>Comparar datas</li>
 * <li>Adicionar um dia � uma data</li>
 * <li>Calcular a quantidade de dias entre duas datas</li>
 * <li>Etc.</li>
 * </ul>
 *
 * @author Gleydson Lima
 * @author David Pereira
 */
public class CalendarUtils {

	/**
	 * Incrementa uma data em um dia
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionaUmDia(Date data) {
		return adicionaDias(data, 1);
	}
	
	/**
	 * Incrementa uma data em uma quantidade de dias passada como par�metro
	 * 
	 * @param data
	 * @return
	 */
	public static Date adicionaDias(Date data, int dias) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DAY_OF_MONTH, dias);
		return c.getTime();
	}

	/**
	 * Compara dias datas sem considerar as horas, apenas dia,
	 * m�s e ano.
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static int compareTo(Date data1, Date data2) {
		return (int) -diferencaDias(data1, data2);
	}
	
	/**
	 * Calcula a quantidade de meses entre duas data no m�ximo de 12 meses (1 ano)
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaMesAno(Date dataInicio, Date dataFim){
		int meses = calculoMeses(dataInicio,dataFim);
		if (meses > 12)
			meses = 12;
		return meses;
	}

	/**
	 * M�todo que calcula a quantidade de anos existentes entre duas datas
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculoAnos(Date dataInicio, Date dataFim){
		DateTime inicio = new DateTime(dataInicio.getTime());
		DateTime fim = new DateTime(dataFim.getTime());
		
		return Years.yearsBetween(inicio, fim).getYears();
	}

	/** Retorna a quantidade de dias existentes entre duas datas
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculoDias (Date dataInicio, Date dataFim){
		DateTime inicio = new DateTime(dataInicio.getTime());
		DateTime fim = new DateTime(dataFim.getTime());
		
		return Days.daysBetween(inicio, fim).getDays();
	}

	/**
	 * Faz o c�lculo da quantidade de dias existentes entre duas datas,
	 * Incluindo o dia de in�cio e o dia de t�rmino na contagem
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 * @deprecated Usar o m�todo calculoDias()
	 */
	@Deprecated
	public static int calculoDiasIntervalosFechados(Date dataInicio, Date dataFim){
		return calculoDias(dataInicio, dataFim) + 1;
	}

	/**
	 * M�todo que calcula a quantidade de meses existentes entre duas datas
	 */
	public static int calculoMeses(Date dataInicio, Date dataFim){
		if(dataFim == null) dataFim = new Date();
		if(dataInicio == null) return 0;
		
		LocalDate inicio = new LocalDate(dataInicio.getTime());
		LocalDate fim = new LocalDate(dataFim.getTime());

		if (fim.dayOfMonth().withMaximumValue().equals(fim))
			return Months.monthsBetween(inicio, fim).getMonths() + 1;
		else
			return Months.monthsBetween(inicio, fim).getMonths();
			
	}

	/**
	 * 
	 * M�todo que configura a tempo de uma data para o tempo passado como argumento.
	 *
	 * @param data ??/??/???? xx:xx:xx:xxx
	 * @param hora HH
	 * @param minuto mm
	 * @param segundos ss
	 * @param milisegundos yyy
	 * 
	 * @return ??/??/???? HH:mm:ss:yyy
	 */
	public static Date configuraTempoDaData(Date data, int hora, int minuto, int segundos, int milisegundos){
		
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.HOUR_OF_DAY, hora);
		c.set(Calendar.MINUTE, minuto);
		c.set(Calendar.SECOND, segundos);
		c.set(Calendar.MILLISECOND, milisegundos);
		
		return c.getTime();
	}

	/**
	 * Retorna, dentre uma cole��o de datas informadas, aquela mais pr�xima de uma data de refer�ncia
	 * 
	 * @param referencia
	 * @param datas
	 * @return
	 */
	public static Date dataMaisProxima(Date referencia, Collection<Date> datas) {
		Date melhorData = null;
		long diff = Long.MAX_VALUE;
	
		for (Date data : datas) {
			if (Math.abs(data.getTime() - referencia.getTime()) < diff) {
				diff = Math.abs(data.getTime() - referencia.getTime());
				melhorData = data;
			}
		}
	
		return melhorData;
	}
	
	/**
	 * Retorna a diferen�a em dias entre duas datas
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 * @deprecated Usar o m�todo calculoDias()
	 */
	@Deprecated
	public static long diferencaDias(Date inicio, Date fim) {
		return calculoDias(inicio, fim);
	}
	
	
	/**
	 * Retorna a diferen�a em meses entre duas datas
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 * @deprecated Usar o m�todo calculoMeses()
	 */
	@Deprecated
	public static long diferencaMeses(Date inicio, Date fim) {
		return calculoMeses(inicio, fim);
	}
	
	
	/**
	 * Retorna o ano atual
	 *
	 * @param data
	 * @return
	 */
	public static Integer getAno(Date data) {
		Calendar c = Calendar.getInstance();
		if (data != null) {
			c.setTime(data);
			return c.get(Calendar.YEAR);
		} else
			return null;
	}

	/**
	 * Retorna o ano corrente
	 * 
	 * @return
	 */
	public static int getAnoAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}

	/**
	 * Retorna uma data contendo o �ltimo dia do m�s e ano passados
	 *
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static Date getMaximoDia(int mes, int ano) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes > 0 ? mes - 1 : mes);
		// primeiro dia do m�s
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar novo = Calendar.getInstance();
		novo.setTime(cal.getTime());
		int max = novo.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cal.set(Calendar.DAY_OF_MONTH, max);
		
		return cal.getTime();
	}

	/**
	 * Retorna o m�s de uma data em seu formado de descri��o abreviado
	 * 
	 * @param d
	 * @return
	 */
	public static String getMesAbreviado(Date d) {
		if (d != null) {
			// MMM � o formato para o nome do m�s, abreviado.  
			DateFormat df = new SimpleDateFormat("MMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return df.format(cal.getTime()).toUpperCase();
		}
	
		return null;
	}

	/**
	 * Retorna o nome abreviado do m�s. Deve-se passar mes entre 1 e 23
	 * @param mes
	 * @return
	 */
	public static String getMesAbreviado(int mes) {
		Calendar cal = Calendar.getInstance();
		mes--;
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.DATE, 1);
		return getMesAbreviado(cal.getTime());
	}
	
	/**
	 * Retorna o m�s de uma data em seu formado de descri��o abreviado
	 * 
	 * @param d
	 * @return
	 */
	public static String getNomeMes(Date d) {
		if (d != null) {
			// MMMMM � o formato para o nome do m�s por extenso
			DateFormat df = new SimpleDateFormat("MMMMM");
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return df.format(cal.getTime());
		}
	
		return null;
	}

	/**
	 * Retorna o nome por extenso do m�s. Deve-se passar mes entre 1 e 23
	 * @param mes
	 * @return
	 */
	public static String getNomeMes(int mes) {
		Calendar cal = Calendar.getInstance();
		mes--;
		cal.set(Calendar.MONTH, mes);
		// seta o calend�rio para o primeiro dia do m�s
		cal.set(Calendar.DATE, 1);
		return getNomeMes(cal.getTime());
	}

	/**
	 * Retorna o m�s atual
	 * 
	 * @return
	 */
	public static int getMesAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH);
	}
	
	/** Retorna a ordem do m�s atual. Ex.: Janeiro = 1, Maio = 5, Dezembro = 12.
	 * @return
	 */
	public static int getMesAtual1a12() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		// adiciona 1 ao m�s pois o Java trata os meses iniciando de 0
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * Retorna o m�s de uma determinada data. Se a data passada for nula, retorna -1.
	 * @param d
	 * @return
	 */
	public static int getMesByData(Date d) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.get(Calendar.MONTH);
		}
		return -1;
	}
	
	/**
	 * Retorna o dia de uma determinada data. Se a data passada for nula, retorna -1.
	 * @param d
	 * @return
	 */
	public static int getDiaByData(Date d) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.get(Calendar.DATE);
		}
		return -1;
	}	

	/**
	 * Retorna o dia da semana de uma data. Se a data passada for nula, retorna -1.
	 * 
	 * @param d
	 * @return
	 */
	public static int getDiaSemanaByData(Date d) {
		if (d != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.get(Calendar.DAY_OF_WEEK);
		}
		return -1;
	}
	
	/**
	 * Verifica se a data atual est� dentro do per�odo informado
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public static boolean isDentroPeriodo(Date inicio, Date fim) {
		return isDentroPeriodo(inicio, fim, new Date());
	}

	/**
	 * Verifica se a data atual est� dentro do per�odo informado
	 * 
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public static boolean isDentroPeriodo(Date inicio, Date fim, Date dataAtual) {
		if (inicio == null || fim == null || dataAtual == null)
			return false;
		dataAtual = CalendarUtils.descartarHoras(dataAtual);
		
		return !(CalendarUtils.compareTo(dataAtual, CalendarUtils.descartarHoras(fim)) > 0 || CalendarUtils.compareTo(dataAtual, CalendarUtils.descartarHoras(inicio))< 0);
	}	
	
	/**
	 * M�todo respons�vel por verificar se dois per�odos informados concomitam entre si.<br />
	 * OBS: Caso alguma das datas seja nula, ser� considerado que � um intervalo aberto, ou seja,
	 * considerando que:
	 *  - inicio1 = 01/05/2008; termino1 = 31/05/2008; inicio2 = null; termino2 = 05/05/2008;
	 * ser� considerado que inicio2 � anterior a inicio1, e como termino2 � posterior a inicio1 a data conflitar�.
	 * @param inicio1
	 * @param termino1
	 * @param inicio2
	 * @param termino2
	 * @return
	 */
	public static boolean isIntervalosDeDatasConflitantes(Date inicio1, Date termino1, Date inicio2, Date termino2){

		if(inicio2 == null && termino2 == null){
			return true;
		}

		if(inicio1 == null && termino1 == null){
			return true;
		}

		if(inicio1 == null && inicio2 == null)
			return true;

		if(termino1 == null && termino2 == null)
			return true;

		if(inicio1 == null && termino2 == null){
			if(termino1 != null && !termino1.before(inicio2))
				return true;
		}

		if(termino1 == null && inicio2 == null){
			if(inicio1 != null && !inicio1.after(termino2))
				return true;
		}

		if(termino1 == null){
			if(inicio1 != null && !inicio1.after(termino2))
				return true;
		}else if(termino2 == null){
			if(!termino1.before(inicio2))
				return true;
		}else if(inicio1 == null){
			if(!termino1.before(inicio2))
				return true;
		}else if(inicio2 == null){
			if(!termino2.before(inicio1))
				return true;
		}else{
			if(!termino1.after(termino2) && !termino1.before(inicio2))
				return true;
			if(inicio1.before(inicio2) && termino1.after(termino2))
				return true;
			if(!inicio1.after(termino2) && !inicio1.before(inicio2))
				return true;
			if (inicio1.equals(inicio2) && termino1.equals(termino2))
				return true;
		}

		return false;
	}

	/**
	 * Converte uma data em String no formato yyyy-MM-dd para um objeto Date 
	 * 
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(data);
	}

	/**
	 * Converte uma data em formato String para um objeto Date, de acordo com a m�scara informada
	 * 
	 * @param data
	 * @param formato
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String data, String formato) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.parse(data);
	}

	/**
	 * Retorna a quantidade de finais de semana entre duas datas.
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int quantidadeFds(Date dataInicio, Date dataFim) {
		int total = 0;

		Calendar dataInicial = Calendar.getInstance();
		dataInicial.setTime(dataInicio);
		
		Calendar dataFinal = Calendar.getInstance();
		dataFinal.setTime(dataFim);
		dataFinal.add(Calendar.DAY_OF_MONTH, 1);

		//contando o n�mero de fds.
		while(!DateUtils.isSameDay(dataInicial, dataFinal)) {
			// se � s�bado ou domingo, adiciona um dia � data
			if (isWeekend(dataInicial)) total++;
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return total;
	}

	/**
	 * Adiciona uma quantidade de meses a uma data
	 * 
	 * @param data
	 * @param qtdMeses
	 * @return
	 */
	public static Date somaMeses(Date data, int qtdMeses) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MONTH, qtdMeses);
		return c.getTime();
	}

	/**
	 * Retorna inst�ncia de Calendar
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @param hora
	 * @param min
	 * @return
	 */
	public static Calendar getInstance(int dia, int mes, int ano, int hora, int min) {
		Calendar cal = Calendar.getInstance();
		// subtrai 1 do m�s, pois o Java trata os meses iniciando em 0.
		cal.set(ano, mes - 1, dia, hora, min, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * Retorna inst�ncia de Calendar
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static Calendar getInstance(int dia, int mes, int ano) {
		Calendar cal = Calendar.getInstance();
		// subtrai 1 do m�s, pois o Java trata os meses iniciando em 0.
		cal.set(ano, mes - 1, dia, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * Retorna uma string formatada de uma data.
	 * 
	 * @param cal
	 * @param formatoSimples
	 * @return
	 */
	public static String format(Calendar cal, boolean formatoSimples) {
		if (formatoSimples) {
			DateFormat df = new SimpleDateFormat();
			return df.format(cal.getTime());
		} else {
			DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
			return df.format(cal.getTime());
		}
	}

	/**
	 * Formata uma data de acordo com o pattern desejado
	 * 
	 * @param cal
	 * @param formato
	 * @return
	 */
	public static String format(Calendar cal, String formato) {
		DateFormat df = new SimpleDateFormat(formato);
		return df.format(cal.getTime());
	}
	
	/**
	 * Retorna String com data formata
	 * @param data
	 * @param formato
	 * @return
	 */
	public static String format(Date data, String formato) {
		DateFormat df = new SimpleDateFormat(formato);
		return df.format(data);
	}

	/**
	 * Retorna instancia de Calendar
	 * 
	 * @param pattern
	 * @param valor
	 * @return
	 */
	public static Calendar getInstance(String pattern, String valor)  {
		DateFormat df = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(df.parse(valor));
			cal.set(Calendar.MILLISECOND, 0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}

	/**
	 * Diz se � fim de semana
	 * 
	 * @param cal
	 * @return
	 */
	public static boolean isWeekend(Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
	}
	
	/**
	 * Retorna true se a data passada como par�metro for um fim de semana.
	 * @param data
	 * @return
	 */
	private static boolean isWeekend(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		return isWeekend(cal);
	}	

	/**
	 * Adiciona uma quantidade de dia
	 * @param cal
	 * @param dayWeek
	 */
	public static void setNextDayWeek(Calendar cal, int dayWeek) {
		// verifica a diferen�a de dias entre as duas semanas
		int dif = dayWeek - cal.get(Calendar.DAY_OF_WEEK);
		// se for positiva, adiciona os dias ao calend�rio
		if (dif > 0) {
			cal.add(Calendar.DAY_OF_WEEK, dif);
		} else {
			// se for negativa, adiciona uma semana ao calend�rio (7 dias) 
			cal.add(Calendar.DAY_OF_WEEK, 7 + dif);
		} 
	}

	/**
	 * Adiciona uma quantidade de dia
	 * 
	 * @param c
	 * @param dayWeek
	 * @return
	 */
	public static Calendar getNextDayWeek(Calendar c, int dayWeek) {
		Calendar cal = (Calendar) c.clone();
		// verifica a diferen�a de dias entre as duas semanas
		int dif = dayWeek - cal.get(Calendar.DAY_OF_WEEK);
		// se for positiva, adiciona os dias ao calend�rio
		if (dif > 0) {
			cal.add(Calendar.DAY_OF_WEEK, dif);
		} else {
			// se for negativa, adiciona uma semana ao calend�rio (7 dias) 
			cal.add(Calendar.DAY_OF_WEEK, 7 + dif);
		} 
		return cal;
	}

	/**
	 * 
	 * Compara se a data j� passou do prazo, levando em considera��o apenas 
	 * o dia/m�s/ano.
	 * 
	 * Usado muito na compara��o de prazo para devolu��o de empr�stimos.
	 *
	 * @param prazo
	 * @param dataComparacao normalmente a data atual
	 * @return
	 */
	public static boolean estorouPrazo(Date prazo, Date comparacao ){
		Calendar cPrazo = Calendar.getInstance();
		cPrazo.setTime(prazo);
		
		Calendar cComparacao = Calendar.getInstance();
		cComparacao.setTime(comparacao);
		
		// J� est� atrasado.
		if (cComparacao.get(Calendar.YEAR) > cPrazo.get(Calendar.YEAR) ) return true;
		
		// Ainda est� adiantado.
		if (cComparacao.get(Calendar.YEAR) < cPrazo.get(Calendar.YEAR) ) return false;
		
		// Se for igual, olha o m�s e assim sucessivamente.
		if (cComparacao.get(Calendar.MONTH) > cPrazo.get(Calendar.MONTH) ) return true;
		
		if (cComparacao.get(Calendar.MONTH) < cPrazo.get(Calendar.MONTH) ) return false;

		if (cComparacao.get(Calendar.DAY_OF_MONTH) > cPrazo.get(Calendar.DAY_OF_MONTH) ) return true;
		
		if (cComparacao.get(Calendar.DAY_OF_MONTH) < cPrazo.get(Calendar.DAY_OF_MONTH) ) return false;
		
		// Se o dia for igual, ainda est� no prazo.
		if (cComparacao.get(Calendar.DAY_OF_MONTH) == cPrazo.get(Calendar.DAY_OF_MONTH) ) return false;

		
		return false; // Nunca era para chegar aqui.
	}

	/**
	 * Indica se a data j� passou do prazo, levando em considera��o o dia/m�s/ano hora:minutos.
	 * 
	 * Para o caso do prazo ser contado em horas, n�o em dias
	 * 
	 * @param prazo
	 * @param dataComparacao
	 *            normalmente a data atual
	 * @return
	 */
	public static boolean estorouPrazoConsiderandoHoras(Date prazo, Date dataComparacao ){
		
		Calendar cPrazo = Calendar.getInstance();
		cPrazo.setTime(prazo);
		
		Calendar cComparacao = Calendar.getInstance();
		cComparacao.setTime(dataComparacao);
		
		
		// j� esta atrasado
		if(cComparacao.get(Calendar.YEAR) > cPrazo.get(Calendar.YEAR) ) return true;
		
		// ainda esta adiantado
		if(cComparacao.get(Calendar.YEAR) < cPrazo.get(Calendar.YEAR) ) return false;
		
		// se for igual olha o m�s e assim sucessivamente
		if(cComparacao.get(Calendar.YEAR) == cPrazo.get(Calendar.YEAR) ){
		
			if(cComparacao.get(Calendar.MONTH) > cPrazo.get(Calendar.MONTH) ) return true;
			
			if(cComparacao.get(Calendar.MONTH) < cPrazo.get(Calendar.MONTH) ) return false;
		
			if(cComparacao.get(Calendar.MONTH) == cPrazo.get(Calendar.MONTH) ){
				
				if(cComparacao.get(Calendar.DAY_OF_MONTH) > cPrazo.get(Calendar.DAY_OF_MONTH) ) return true;
				
				if(cComparacao.get(Calendar.DAY_OF_MONTH) < cPrazo.get(Calendar.DAY_OF_MONTH) ) return false;
				
				if(cComparacao.get(Calendar.DAY_OF_MONTH) == cPrazo.get(Calendar.DAY_OF_MONTH) ) {
					
					if(cComparacao.get(Calendar.HOUR_OF_DAY) > cPrazo.get(Calendar.HOUR_OF_DAY)) return true;
					
					if(cComparacao.get(Calendar.HOUR_OF_DAY) < cPrazo.get(Calendar.HOUR_OF_DAY)) return false;
					
					if(cComparacao.get(Calendar.HOUR_OF_DAY) == cPrazo.get(Calendar.HOUR_OF_DAY)){
						
						if(cComparacao.get(Calendar.MINUTE) > cPrazo.get(Calendar.MINUTE)) return true;
						
						if(cComparacao.get(Calendar.MINUTE) < cPrazo.get(Calendar.MINUTE)) return false;
						
						// se for igual ainda est� no prazo
						if(cComparacao.get(Calendar.MINUTE) == cPrazo.get(Calendar.MINUTE)) return false;
						
					}
					
				}
				
			}
			
		}
		
		return false; // nunca era para chegar aqui
		
	}

	/**
	 * M�todo que calcula a quantidade de horas existentes entre duas datas
	 *
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaQuantidadeHorasEntreDatas(Date dataInicio, Date dataFim){
	
		if(dataFim == null)
			dataFim = new Date();
	
		long horas;
	
		if(dataFim.getTime() > dataInicio.getTime()){
			// diferen�a entre as duas datas em milisegundos.
			horas= dataFim.getTime() - dataInicio.getTime();
			// dividindo por:
			horas/=1000; // obtemos os segundos entre as datas
			horas/=60; // obtemos os minutos entre as datas
			horas/=60; // obtemos os horas entre as datas
			
		}else{
			horas = 0;
		}
	
		return (int) horas;
	}

	/**
	 * No intervalo eu considero o primeiro dia. <br>
	 * Ex 1: entre 18 e 18 = 1 dia; <br>
	 * Ex 2: entre 17 e 20 = (18,19,20) 3 dias, mas eu considero que o dia 17 � um dia �til, ent�o fica (17,18,19,20) 4 dias.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 * @deprecated Usar o m�todo calculaQuantidadeDiasEntreDatasIntervaloFechado()
	 */
	@Deprecated
	public static int calculaQuantidadeDiasPeriodo(Date dataInicio, Date dataFim){
		return calculoDias(dataInicio, dataFim) + 1;
	}

	/**
	 * Retorna a quantidade de dias entre duas datas, arredondando o n�mero de horas entre as datas para cima.<br/>
	 * Ex.: se a data inicial for 01/01/2001 15:00 e a final for 02/01/2001 09:00, retornar� um dia.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaQuantidadeDiasEntreDatasIntervaloFechado(Date dataInicio, Date dataFim){
		return calculoDias(dataInicio, dataFim) + 1;
	}

	/** Indica se o ano � bissexto.
	 * @param ano
	 * @return
	 */
	public static boolean isAnoBissexto(int ano) {
		// se o ano for divis�vel por 4, n�o for divis�vel por 100 e for divis�vel por 400, � bissexto
		return ano%4==0 && ( ano%400 == 0 || ano%100 != 0);
	}

	/**
	 * Retorna a quantidade de semanas entre duas datas.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaQuantidadeSemanasEntreDatasIntervaloFechado(Date dataInicio, Date dataFim){
		DateTime inicio = new DateTime(dataInicio.getTime());
		DateTime fim = new DateTime(dataFim.getTime());
		
		return Weeks.weeksBetween(inicio, fim).getWeeks();
	}

	/**
	 * Retorna a quantidade de meses, em intervalo fechado, entre duas datas.<br/>
	 * Exempplo: entre as datas 10/01/2008 e 01/12/2009 existem 24 meses.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaQuantidadeMesesEntreDatasIntervaloFechado(Date dataInicio, Date dataFim){
		return calculoMeses(dataInicio, dataFim) + 1;
	}

	/**
	 * Retorna a quantidade de anos entre duas data com intervalo fechado.
	 *
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaQuantidadeAnosEntreDatasIntervaloFechado(Date dataInicio, Date dataFim){
		return calculoAnos(dataInicio, dataFim) + 1;
	}

	/**
	 * Adiciona os dias do fim semana � um calend�rio.<br/>
	 * M�todo que SEMPRE deve ser chamado quando o prazo de um empr�stimo
	 * precisar ser criado ou atualizado para verificar se o dia do prazo cai
	 * num s�bado ou domingo. Nete caso, ele seta o prazo para a segunda-feira
	 * pr�xima, pois o usu�rio n�o pode devolver nada nos finais de semana.
	 * 
	 * @param calendar
	 */
	public static void adicionaDiasFimDeSemana(Calendar calendar){
		// se o dia de semana � s�bado, adiciona dois dias
		if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			calendar.add(Calendar.DAY_OF_WEEK, 2);
		}else{
			// se o dia de semana � domingo, adiciona um dia.
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				calendar.add(Calendar.DAY_OF_WEEK, 1);
			}
		}
	}

	/**
	 * Diminui a data passada em um ano.
	 *
	 * @param data
	 * @return
	 */
	public static Date diminuiDataEmUmAno(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		// seta o calend�rio para o ano anterior
		c.roll(Calendar.YEAR, false);
		return c.getTime();
	}
	
	/**
	 * Retorna a data do primeiro dia do m�s.
	 * @param mes
	 * @return
	 */
	public static Date primeiroDataMes(int mes){
		
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.MONTH, mes);
		// seta o dia do calend�rio para o primeiro dia do m�s
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		return c.getTime();
	}
	
	/**
	 * Retorna a data do �ltimo dia do m�s.
	 * @param mes
	 * @return
	 */
	public static Date ultimaDataMes(int mes){
		
		Calendar c = Calendar.getInstance();
		// calcula o m�s seguinte ao m�s passado no par�metro
		if(mes == Calendar.DECEMBER){
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
		}else{
			c.set(Calendar.MONTH, mes+1);
		}
		// seta a data do m�s seguinte para o dia primeiro
		c.set(Calendar.DAY_OF_MONTH, 1);
		// subtrai um dia do calend�rio. Assim, temos o �ltimo dia do m�s
		c.add(Calendar.DAY_OF_MONTH,-1);
		
		return c.getTime();
	}
	
	/**
	 * Cria uma data a partir do dia, m�s e ano passados como par�metro.
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static Date createDate(int dia, int mes, int ano) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, ano);
		c.set(Calendar.MONTH, mes);
		c.set(Calendar.DAY_OF_MONTH, dia);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * Deixa um objeto Date apenas com os campos de dia, m�s e ano.
	 * @param data
	 * @return
	 */
	public static Date descartarHoras(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c = descartarHoras(c);
		return c.getTime();
	}
	
	/**
	 * Deixa um objeto Date apenas com os campos de dia, m�s e ano.
	 * @param data
	 * @return
	 */
	public static Calendar descartarHoras(Calendar c) {
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;
	}
	
	/**
	 * Deixa um objeto Date com hora, minutos e segundos ao fim do dia.
	 * @param data
	 * @return
	 */
	public static Date incluirHoraFimDia(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		// seta a hora do calend�rio para 23:59:59 (�ltima hora do dia)
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);

		return c.getTime();
	}

	/**
	 * Retorna uma string com o dia da semana correspondente � data
	 * passada como par�metro. A string pode estar em formato completo
	 * ou abreviado, dependendo do par�metro abreviado.
	 * 
	 * @param data
	 * @param abreviado
	 * @return
	 */
	public static String diaSemana(Date data, boolean abreviado) {
		return new SimpleDateFormat(abreviado ? "EEE" : "EEEEE").format(data);
	}

	/**
	 * Adiciona uma quantidade de meses � data informada e retorna a nova data 
	 * @param data
	 * @param qtdMeses
	 * @return
	 */
	public static Date adicionaMeses(Date data, Integer qtdMeses) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MONTH, qtdMeses);
		return c.getTime();
	}
	
	/**
	 * Subtrai uma quantidade de meses � data informada e retorna a nova data 
	 * @param data
	 * @param qtdMeses
	 * @return
	 */
	public static Date subtrairMeses(Date data, Integer qtdMeses) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MONTH, -qtdMeses);
		return c.getTime();
	}

	/**
	 * Identifica se duas datas correspondem ao mesmo dia do ano.
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static boolean isSameDay(Date data1, Date data2) {
		
		if (data1 == null && data2 == null) {
			return true;
		} else if ((data1 == null && data2 != null) || (data1 != null && data2 == null)) {
			return false;
		} else {
			return DateUtils.isSameDay(data1, data2);
		}
		
	}

	/**
	 * Subtrai uma determinada quantidade de dias de uma data.
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date subtraiDias(Date data, int dias) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DAY_OF_MONTH, -dias);
		return c.getTime();
	}

	/**
	 *Adiciona a uma determinada data a quantidade de anos informada.
	 * 
	 * @param data
	 * @param anos
	 * @return
	 */
	public static Date adicionarAnos (Date data, int anos) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.YEAR, anos);
		return c.getTime();		
	}

	/**
	 * Remove de uma determinada data a quantidade de anos informada.
	 * @param data
	 * @param anos
	 * @return
	 */
	public static Date subtrairAnos (Date data, int anos) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.YEAR, -anos);
		return c.getTime();	
	}
	
	/**
	 * Retorna a hora atual do sistema.
	 * @return
	 */
	public static Integer getHoraAtual() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * Verifica se os meses das duas datas informadas s�o consecutivos (ex: data = janeiro/2010, outraData = fevereiro/2010 e vice-versa.
	 * 
	 * @param data
	 * @param outraData
	 * @return
	 */
	public static boolean isMesesConsecutivos(Date data, Date outraData){
		Calendar calData = Calendar.getInstance();
		calData.setTime(CalendarUtils.descartarHoras(data));
		Calendar calOutraData = Calendar.getInstance();
		calOutraData.setTime(CalendarUtils.descartarHoras(outraData));
		// seta os dias nos calend�rios para o primeiro dia do m�s
		calData.set(Calendar.DAY_OF_MONTH, 1);
		calOutraData.set(Calendar.DAY_OF_MONTH, 1);
		// retorna um m�s no calend�rio
		calData.add(Calendar.MONTH, -1);
		// se as datas s�o iguais, retorna true
		if(calData.equals(calOutraData))
			return true;
		// avan�a um m�s no calend�rio 
		calData.add(Calendar.MONTH, 1);
		// retorna um m�s no outro calend�rio
		calOutraData.add(Calendar.MONTH, -1);
		// se as datas s�o iguais, retorna true
		if(calData.equals(calOutraData))
			return true;
		return false;
	}
	
	/**
	 * Converte um n�mero de 2 d�gitos representando um ano para sua vers�o
	 * completa de 4 d�gitos.
	 *
	 * @param ano
	 * @return
	 */
	public static int completaAno(int ano) {
		// se for um ano menor que 100. Exemplos: 95; 5
		if (ano < 100) {
			Calendar c = Calendar.getInstance();
			String anoAtual = String.valueOf(c.get(Calendar.YEAR));
			// adiciona o mil�nio e o centen�rio atual ao ano. Exemplos: 1900 + 95; 2000 + 95; 2100 + 05
			// se o ano passado por par�metro for menor que 10, adicionar um zero � frente. Exemplo: 2000 + "0" + 5
			return Integer.parseInt(anoAtual.substring(0, 2) + (ano >= 10 ? ano : ("0" + ano)));
		}

		throw new IllegalArgumentException(
				"S� � poss�vel completar o valor de anos menores que 100.");
	}
	
	/**
	 * Retorna a quantidade de dias �teis entre duas datas, tirando os fins
	 * de semana e feriados.
	 * @return
	 */
	public static int getDiasUteisEntreDatas(Date inicio, Date fim) {
		int total = calculaQuantidadeDiasPeriodo(inicio, fim);
		int fds = quantidadeFds(inicio, fim);
		int qtdFeriados = 0;

		FeriadoDao dao = new FeriadoDao();
		
		try {
			List<Feriado> feriados = dao.findFeriadosPorIntervaloDatas(inicio, fim);
			if (!isEmpty(feriados)) {
				for (Feriado f : feriados) {
					if (!isWeekend(f.getDataFeriado()))
						qtdFeriados++;
				}
			}
		} catch(DAOException e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return total - fds - qtdFeriados;
	}
	
	/**
	 * Retorna a quantidade de dias �teis entre duas datas, tirando os fins
	 * de semana e feriados considerando cidade e estados..
	 * @return
	 */
	public static int getDiasUteisEntreDatas(Date inicio, Date fim,Integer cidade, Integer estado) {
		int total = calculaQuantidadeDiasEntreDatasIntervaloFechado(inicio, fim);
		int fds = quantidadeFds(inicio, fim);
		int qtdFeriados = 0;

		FeriadoDao dao = new FeriadoDao();
		
		try {
			List<Feriado> feriados = dao.findByPeriodoFeriadosLocalidade(inicio, fim,cidade,estado);
			if (!isEmpty(feriados)) {
				for (Feriado f : feriados) {
					if (!isWeekend(f.getDataFeriado()))
						qtdFeriados++;
				}
			}
		} catch(DAOException e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return total - fds - qtdFeriados;
	}
	
	/**
	 * Adiciona uma determinada quantidade de dias �teis (sem considerar
	 * fins de semana e feriados) a uma data.
	 * 
	 * @param data
	 * @param qtdDiasUteis
	 * @return
	 */
	public static Date adicionarDiasUteis(Date data, int qtdDiasUteis) {
		int diasAdicionados = 0;
		Date dataAtual = data;
		
		while (diasAdicionados < qtdDiasUteis) {
			dataAtual = adicionaDias(dataAtual, 1);
			if (!isWeekend(dataAtual) && !isFeriado(dataAtual))
				diasAdicionados++;
		}
		
		return dataAtual;
	}
	
	/**
	 * Retorna true se uma data for feriado e false caso contr�rio.
	 * @param data
	 * @return
	 */
	public static boolean isFeriado(Date data) {
		FeriadoDao dao = new FeriadoDao();
		
		try {
			Collection<Feriado> feriados = dao.findByDataFeriado(data);
			return !isEmpty(feriados);
		} catch(Exception e) {
			return false;
		} finally {
			dao.close();
		}
	}

	/**
	 * Subtrai uma determinada quantidade de dias �teis (sem considerar
	 * fins de semana e feriados) a uma data.
	 * 
	 * @param data
	 * @param qtdDiasUteis
	 * @return
	 */
	public static Date subtrairDiasUteis(Date data, int qtdDiasUteis) {
		int diasSubtraidos = 0;
		Date dataAtual = data;
		
		while (diasSubtraidos < qtdDiasUteis) {
			// subtrai um dia da data atual
			dataAtual = subtraiDias(dataAtual, 1);
			if (!isWeekend(dataAtual) && !isFeriado(dataAtual))
				diasSubtraidos++;
		}
		
		return dataAtual;
	}

	/**
	 * Define o hor�rio (horas e minutos) de uma data a partir do hor�rio de uma segunda data
	 * 
	 * @param data
	 * @param horario
	 * @return
	 */
	public static Date definirHorario(Date data, Date horario) {
		Calendar calendarData = Calendar.getInstance();
		calendarData.setTime(data);
		
		Calendar calendarHorario = Calendar.getInstance();
		calendarHorario.setTime(horario);
		
		calendarData.set(Calendar.HOUR_OF_DAY, calendarHorario.get(Calendar.HOUR_OF_DAY));
		calendarData.set(Calendar.MINUTE, calendarHorario.get(Calendar.MINUTE));
		
		return calendarData.getTime();
	}
	
	/**
	 * Verifica se o valor passado como par�metro � uma data v�lida seguindo os padr�es:
	 * <ul>
	 * <li>dd/MM/yy</li>
	 * <li>dd/MM/yyyy</li>
	 * </ul>
	 * @param data
	 * @return
	 */
	public static boolean isFormatoDataValida(String data){
		Pattern padraoData = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/(\\d{2}?|\\d{4}?)$");
		
		Matcher matcher = padraoData.matcher(data);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	
	/**
	 * Retorna um Set contendo todos os dias �teis (contando os S�bados) entre as datas passadas, inclusive.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 * @throws DAOException
	 */
	public static Set<Date> getDiasUteis(Date dataInicio, Date dataFim) throws DAOException {
		Set<Date> datas = null;
		FeriadoDao dao = DAOFactory.getInstance().getDAO(FeriadoDao.class);
		try{
			datas = new TreeSet<Date>();
			Date dataAuxiliar = dataInicio;
			while(!dataAuxiliar.after(dataFim)) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataAuxiliar);
				if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					datas.add(dataAuxiliar);
				
				dataAuxiliar = adicionaUmDia(dataAuxiliar);
			}
			List<Feriado> feriados = dao.findFeriadosPorIntervaloDatas(dataInicio, dataFim);
			if(!isEmpty(feriados)) {
				for (Feriado feriado : feriados) {
					datas.remove(feriado.getDataFeriado());
				}
			}
		} finally {
			dao.close();
		}
		
		return datas;
	}
	
	/**
	 * M�todo que retorna um String com a dura��o do intervalo entre as datas inicio e fim, no formato: <[99]d> <[99]h> <[99]min>  
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public static String getQtdDiasHorasMinutosIntervalo(Date inicio, Date fim){
		
		if(inicio.after(fim)){
			return "Inv�lido";
		}
		
		DateTime dtInicio = new DateTime(inicio);
		DateTime dtFim = new DateTime(fim);
		Interval interval = new Interval(dtInicio,dtFim);
		
		PeriodFormatter diasHorasMinutos = new PeriodFormatterBuilder()
		.appendYears()
		.appendSuffix("a ")
		.appendMonths()
		.appendSuffix("m ")
		.appendWeeks()
		.appendSuffix("s ")
	    .appendDays()
	    .appendSuffix("d ")
	    .appendHours()
	    .appendSuffix("h ")
	    .appendMinutes()
	    .appendSuffix("min")
	    .toFormatter();

		Period periodo = interval.toPeriod();
		
		
		
		return diasHorasMinutos.print(periodo);
		
	}
	
	/**
	 * M�todo que calcula a diferen�a de minutos entre datas
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public static int calculaMinutos(Date dataInicio, Date dataFim){
		  DateTime inicio = new DateTime(dataInicio.getTime());
		  DateTime fim = new DateTime(dataFim.getTime());
		  
		  return Minutes.minutesBetween(inicio, fim).getMinutes();
	 }
}
