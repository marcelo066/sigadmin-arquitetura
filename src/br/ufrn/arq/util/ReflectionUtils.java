/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 08/07/2007
 */
package br.ufrn.arq.util;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.negocio.validacao.annotations.FieldName;

/**
 * Classe utilit�ria para auxiliar o trabalho com a
 * API de Reflection.
 *
 * @author David Pereira
 * @author Gleydson Lima
 */
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * Dada uma classe e o nome de um atributo, este m�todo retorna
	 * o objeto Field referente ao atributo na classe. Caso o atributo
	 * n�o seja encontrado na classe, ele ser� buscado na superclasse e
	 * assim por diante, at� java.lang.Object.
	 *
	 * Caso o atributo n�o seja encontrado em nenhum local da hierarquia,
	 * o m�todo retorna null.
	 */
	public static Field getField(Class<?> classe, String fieldName) {
		while (classe != null) {
			Field[] fields = classe.getDeclaredFields();

			if (fields != null && fields.length > 0) {
				for (Field f : fields) {
					if (fieldName.equals(f.getName()))
						return f;
				}
			}
			classe = classe.getSuperclass();
		}
		return null;
	}
	
	/**
	 * Dada uma classe e uma express�o, este m�todo retorna
	 * o objeto Field referente ao atributo na classe. Caso a express�o
	 * seja inv�lida, o m�todo retorna null.
	 */
	public static Field getExpressionField(Class<?> classe, String expression) {
		String[] partes = expression.split("\\.");
		Class<?> tipoParte = classe;
		Field atributoParte = null;
		
		for (String parte : partes) {
			atributoParte = getField(tipoParte, parte);
			tipoParte = atributoParte.getType();
		}
		
		return atributoParte;
	}

	/**
	 * Dada uma classe e o nome de um atributo, retorna true
	 * se o atributo for encontrado na classe ou em alguma de suas
	 * superclasses, ou false caso contr�rio.
	 */
	public static boolean hasField(Class<?> classe, String fieldName) {
		return getField(classe, fieldName) != null;
	}

	/**
	 * Dada uma classe e uma anota��o, o m�todo retorna uma lista de objetos Field
	 * com os atributos da classe que possuem a anota��o. Caso a anota��o n�o
	 * seja encontrada em nenhum atributo, a lista � retornada vazia.
	 */
	public static List<Field> findFieldsWithAnnotation(Class<?> classe, Class<?> annotation) {
		List<Field> result = new ArrayList<Field>();

		while (classe != null) {
			Field[] fields = classe.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field f : fields) {
					if (hasAnnotation(f, annotation))
						result.add(f);
				}
			}
			classe = classe.getSuperclass();
		}
		return result;
	}

	/**
	 * Dado um AnnotatedElement (Class, Field ou Method) e uma classe que
	 * representa uma anota��o, retorna true se a anota��o existir no Annotated
	 * Element, e false caso contr�rio.
	 */
	public static boolean hasAnnotation(AnnotatedElement element, Class<?> annotation) {
		return getAnnotation(element, annotation) != null;
	}

	/**
	 * Dado um AnnotatedElement (Class, Field ou Method) e uma classe que
	 * representa uma anota��o, retorna a inst�ncia da anota��o com os
	 * valores que foram definidos para o annotated element.
	 */
	public static Annotation getAnnotation(AnnotatedElement element, Class<?> annotation) {
		Annotation[] annotations = element.getDeclaredAnnotations();
		if (annotations != null && annotations.length > 0) {
			for (Annotation a : annotations) {
				if (annotation.equals(a.annotationType())) {
					return a;
				}
			}
		}
		return null;
	}

	/**
	 * Identifica se um atributo de uma classe � est�tico ou n�o.
	 */
	public static boolean isStatic(Field f) {
		return Modifier.isStatic(f.getModifiers());
	}

	/**
	 * Retorna um campo de um objeto pelo seu nome ou anota��o. Primeiro busca por um nome. Se
	 * n�o encontrar nenhum atributo com o nome, busca por um atributo com a anotacao.
	 */
	public static Field getFieldByNameOrAnnotation(Object obj, String name, Class<?> annotation) {
		Class<?> classe = obj.getClass();
		return getFieldByNameOrAnnotation(classe, name, annotation);
	}
	
	/**
	 * Retorna um campo de um objeto pelo seu nome ou anota��o. Primeiro busca por um nome. Se
	 * n�o encontrar nenhum atributo com o nome, busca por um atributo com a anotacao.
	 */
	public static Field[] getFieldsByNameOrAnnotation(Object obj, String name, Class<?> annotation) {
		Class<?> classe = obj.getClass();
		return getFieldsByNameOrAnnotation(classe, name, annotation);
	}

	/**
	 * Retorna um campo de uma classe pelo seu nome ou anota��o. Primeiro busca por um nome. Se
	 * n�o encontrar nenhum atributo com o nome, busca por um atributo com a anotacao.
	 */
	public static Field getFieldByNameOrAnnotation(Class<?> classe, String name, Class<?> annotation) {
		Field f = getField(classe, name);

		if (f == null) {
			List<Field> fields = findFieldsWithAnnotation(classe, annotation);
			if (fields != null && !fields.isEmpty())
				f = fields.get(0);
		}

		return f;
	}
	
	/**
	 * Retorna um campo de uma classe pelo seu nome ou anota��o. Primeiro busca por um nome. Se
	 * n�o encontrar nenhum atributo com o nome, busca por um atributo com a anotacao.
	 */
	public static Field[] getFieldsByNameOrAnnotation(Class<?> classe, String name, Class<?> annotation) {
		Field f = getField(classe, name);
		List<Field> campos = new LinkedList<Field>();

		if (f == null) {
			List<Field> fields = findFieldsWithAnnotation(classe, annotation);
			if (fields != null && !fields.isEmpty()) {
				campos.addAll(fields);
			}
		} else {
			campos.add(f);
		}

		return campos.toArray(new Field[campos.size()]);
	}
	
	/**
	 * Altera o valor de um atributo em um objeto. � necess�rio passar o objeto
	 * cujo atributo ter� o valor alterado, um objeto Field e o valor
	 * a ser setado no field.
	 */
	public static void setFieldValue(Object object, Field field, Object value) {

		try {
			// Seta o atributo para acess�vel, se nao estiver
			boolean changedAccessibility = false;
			if (!field.isAccessible()) {
				field.setAccessible(true);
				changedAccessibility = true;
			}

			field.set(object, value);

			// Volta o atributo para nao acessivel, se tiver mudado
			if (changedAccessibility) {
				field.setAccessible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("N�o foi poss�vel alterar o valor do atributo " + field.getName() + ": " + e.getMessage());
		}

	}

	/**
	 * Altera o valor de um atributo em um objeto. � necess�rio passar o objeto
	 * cujo atributo ter� o valor alterado, uma string com o nome do atributo e o valor
	 * a ser setado nele.
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		Field field = getField(object.getClass(), fieldName);
		setFieldValue(object, field, value);
	}

	/**
	 * Retorna o valor de um atributo em um objeto. � necess�rio
	 * passar o objeto e um Field representando o atributo.
	 */
	public static Object getFieldValue(Object object, Field field) {

		try {

			// Seta o atributo para acess�vel, se nao estiver
			boolean changedAccessibility = false;
			if (!field.isAccessible()) {
				field.setAccessible(true);
				changedAccessibility = true;
			}

			Object value = field.get(object);

			// Volta o atributo para nao acessivel, se tiver mudado
			if (changedAccessibility) {
				field.setAccessible(false);
			}

			return value;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("N�o foi poss�vel pegar o valor do atributo " + field.getName() + ": " + e.getMessage());
		}

	}

	/**
	 * Retorna o valor de um atributo em um objeto. � necess�rio
	 * passar o objeto e uma string com o nome do atributo.
	 */
	public static Object getFieldValue(Object object, String nomeAtributo) {
		return getFieldValue(object, getField(object.getClass(), nomeAtributo));
	}
	
	/**
	 * Retorna o nome do campo de acordo como definido na anota��o
	 * {@link FieldName}. Caso a anota��o n�o esteja presente ou o seu valor
	 * n�o tiver sido informado, retorna o nome do atributo.
	 * @param field
	 * @return
	 */
	public static String getFieldName(Field field) {
		FieldName fieldname = (FieldName) getAnnotation(field, FieldName.class);
		
		if (isEmpty(fieldname) || isEmpty(fieldname.value())) {
			return br.ufrn.arq.util.StringUtils.humanFormat(field.getName());
		} else {
			return fieldname.value();
		}
	}

	/**
	 * Retorna um objeto Method de uma classe de acordo com o nome passado
	 * como par�metro. Se o m�todo n�o for encontrado, retorna null.
	 */
	public static Method getMethod(Class<?> classe, String methodName) {
		Method[] methods = classe.getDeclaredMethods();

		if (methods != null && methods.length > 0) {
			for (Method m : methods) {
				if (methodName.equals(m.getName()))
					return m;
			}
		}

		return null;
	}

	/**
	 * Retorna uma String que representa o valor de um atributo dos par�metros   
	 * de uma anota��o do Hibernate.
	 */
	public static String getValuesParameterHibernate(Parameter[] parametros,
			String atributo) {

		for (int i = 0; i < parametros.length; i++)
			if (parametros[i].toString().contains(atributo))
				return parametros[i].toString().substring(
						parametros[i].toString().lastIndexOf(atributo + "=")
								+ atributo.length() + 1,
						parametros[i].toString().lastIndexOf(")"));

		return new String();

	}

	/**
	 * Retorna um objeto que representa o valor do atributo de uma anota��o
	 */
	public static Object getValueAnnotation(Annotation an, String atributo) {
		try {
			if (an != null) {
				for (Method m : an.annotationType().getDeclaredMethods()) {
					if (m.getName().equals(atributo))
						return m.invoke(an);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}
	
	
	/**
	 * Dado um objeto e uma string com o nome de um m�todo,
	 * invoca o m�todo e retorna o valor retornado por ele. Funciona
	 * mesmo se o m�todo for privado.
	 */
	public static Object getMethodReturnValue(Object object, String methodName) {

		Method method = getMethod(object.getClass(), methodName);

		try {

			// Seta o atributo para acess�vel, se nao estiver
			boolean changedAccessibility = false;
			if (!method.isAccessible()) {
				method.setAccessible(true);
				changedAccessibility = true;
			}

			Object value = method.invoke(object, (Object[]) null);

			// Volta o atributo para nao acessivel, se tiver mudado
			if (changedAccessibility) {
				method.setAccessible(false);
			}

			return value;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("N�o foi poss�vel pegar o valor do m�todo " + method.getName() + ": " + e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParameterizedTypeClass(Object o) {
		Class<T> classe = null;
		Type type = o.getClass().getGenericSuperclass();
		loop:
			while (true) {
				if (type instanceof ParameterizedType) {
					Type[] arguments = ((ParameterizedType)type).getActualTypeArguments();
					for (Type argument : arguments) {
						if (argument instanceof Class) {
							classe = (Class<T>) argument;
							break loop;
						}
					}
				} else {
					try {
						Method classField = o.getClass().getDeclaredMethod("getClasse");
						classe = (Class<T>) classField.invoke(o);
						break loop;
					} catch (Exception e) {
					}
				}
				type = ((Class<?>)type).getGenericSuperclass();
				if (type == null || type == Object.class) {
					// throw new RuntimeException("N�o foi poss�vel recuperar a classe do tipo parametrizado");
					return null;
				}
			}
		return classe;
	}

	public static <T> T instantiateClass(Class<T> classe) {
		try {
			return classe.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("N�o foi poss�vel criar uma nova inst�ncia da classe '" + classe.getName() + "'.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		try {
			return (T) ClassUtils.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("N�o foi poss�vel criar uma nova inst�ncia da classe '" + className + "'.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValorConstante(String classe, String constante) {
		try {
			Class<?> clazz = ClassUtils.forName(classe);
			Field field = getField(clazz, constante);
			return (T) getFieldValue(null, field);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> classForName(String classe) {
		try {
			return (Class<T>) Class.forName(classe);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("N�o foi poss�vel encontrar a classe '" + classe + "'.");
		}
	}

	public static Object getProperty(Object obj, String nomeAtributo) {
		try {
			return PropertyUtils.getProperty(obj, nomeAtributo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("N�o foi poss�vel pegar a propriedade " + nomeAtributo + " do objeto " + obj);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("N�o foi poss�vel pegar a propriedade " + nomeAtributo + " do objeto " + obj);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("N�o foi poss�vel pegar a propriedade " + nomeAtributo + " do objeto " + obj);
		}
	}

	public static void setProperty(Object obj, String nomeAtributo, Object valor) {
		try {
			PropertyUtils.setProperty(obj, nomeAtributo, valor);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasGetterSetter(PersistDB obj, String nomeAtributo) {
		Method getter = getMethod(obj.getClass(), "get" + StringUtils.capitalize(nomeAtributo));
		Method setter = getMethod(obj.getClass(), "set" + StringUtils.capitalize(nomeAtributo));
			
		return getter != null && setter != null;
	}

	public static void copyProperties(final Object orig, final Object dest) {
		org.springframework.util.ReflectionUtils.doWithFields(dest.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				setFieldValue(dest, field, getFieldValue(orig, field));
			}
		}, new FieldFilter() {
			public boolean matches(Field field) {
				return !(Modifier.isStatic(field.getModifiers()) ||
						Modifier.isFinal(field.getModifiers())) && !hasAnnotation(field, Autowired.class);
			}
		});
	}

	public static Class<?> getPropertyType(Object obj, String nomeAtributo) {
		try {
			return PropertyUtils.getPropertyType(obj, nomeAtributo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("N�o foi poss�vel pegar o tipo da propriedade " + nomeAtributo + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RuntimeException("N�o foi poss�vel pegar o tipo da propriedade " + nomeAtributo + ": " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("N�o foi poss�vel pegar o tipo da propriedade " + nomeAtributo + ": " + e.getMessage());
		}
	}

	/**
	 * Verifica se uma classe tem um determinado atributo. Muito �til, por
	 * exemplo, quando se quer verificar o atributo "ativo" no DAO, onde s�
	 * temos a classe e n�o o objeto.
	 *
	 * @author Edson Anibal (ambar@info.ufrn.br)
	 */
	public static String evalProperty(Object obj, String property) {
		try {
			property = property.trim();
			StringTokenizer st = new StringTokenizer(property, ".");
			String propertyInsideToken;

			Object atual = obj;
			Class<?> c = obj.getClass();

			while (st.hasMoreTokens()) {
				propertyInsideToken = st.nextToken();
				atual = c.getMethod(formatMethod(propertyInsideToken),
						(Class[]) null).invoke(atual, (Object[]) null);
				if (atual == null) {
					return "";
				}

				c = atual.getClass();

			}

			return atual.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Recupera o valor de uma propriedade de um objeto de acordo com a
	 * express�o informada. <br>
	 * Ex.: 'pessoa.nome' em um objeto servidor ir� tentar acessar
	 * servidor.getPessoa().getNome()
	 *
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Object evalPropertyObj(Object obj, String property) {
		try {
			property = property.trim();
			StringTokenizer st = new StringTokenizer(property, ".");
			String propertyInsideToken;

			Object atual = obj;
			Class<?> c = obj.getClass();

			while (st.hasMoreTokens()) {
				propertyInsideToken = st.nextToken();
				try {
					atual = c.getMethod(formatMethod(propertyInsideToken),
							(Class[]) null).invoke(atual, (Object[]) null);
				} catch (Exception e) {
					// tenta o booleano
					atual = c.getMethod(
							formatBooleanMethod(propertyInsideToken),
							(Class[]) null).invoke(atual, (Object[]) null);
				}
				if (atual == null) {
					return null;
				}

				c = atual.getClass();

			}

			return atual;

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Diz se uma determinada classe tem um determinado atributo. Muito �til
	 * para verificar, por exemplo, se um objeto tem o atributo "ativo".
	 *
	 * @author Edson Anibal (ambar@info.ufrn.br)
	 */
	public static Boolean propertyExists(Class<?> classe, String propriedade) {
		Method[] metodos = classe.getMethods();
		for (Method metodo : metodos)
			if (metodo.getName().equalsIgnoreCase(
					"set" + propriedade.toLowerCase()))
				return true;
		return false;
	}

	/**
	 * Transforma o nome de um atributo no nome de seu m�todo de acesso.<br>
	 * Ex: 'nome' ser� formatado em 'getNome'
	 *
	 * @param field
	 * @return
	 */
	private static String formatMethod(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	/**
	 * Transforma o nome de um atributo booleano no nome de seu m�todo de
	 * acesso.<br>
	 * Ex: 'nome' ser� formatado em 'isNome'
	 *
	 * @param field
	 * @return
	 */
	private static String formatBooleanMethod(String field) {
		return "is" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}	
	
}
