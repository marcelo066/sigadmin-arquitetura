/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 20/10/2009
 */
package br.ufrn.arq.negocio.validacao;

import static br.ufrn.arq.util.ReflectionUtils.classForName;
import static br.ufrn.arq.util.ReflectionUtils.findFieldsWithAnnotation;
import static br.ufrn.arq.util.ReflectionUtils.getAnnotation;
import static br.ufrn.arq.util.ReflectionUtils.getMethod;
import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import br.ufrn.arq.util.StringUtils;

/**
 * Classe que d� suporte ao uso de valida��o por anota��es.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class AnnotationValidationSupport {

	private static final String ANNOTATION_PACKAGE = "br.ufrn.arq.negocio.validacao.annotations";
	
	private static final String VALIDATION_METHOD_PREFIX = "validate";
	
	/**
	 * Valida um objeto procurando por anota��es de valida��o nos
	 * atributos e aplicando as regras de acordo com a valida��o.
	 * @param obj
	 * @param lista
	 */
	public static void validate(final Object obj, final ListaMensagens lista) {
		final List<Field> fieldWithErrors = new ArrayList<Field>();
		
		ReflectionUtils.doWithMethods(AnnotationValidation.class, new ReflectionUtils.MethodCallback() {
			public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
				if (!method.getName().equals(VALIDATION_METHOD_PREFIX) && method.getName().startsWith(VALIDATION_METHOD_PREFIX)) {
					Class<?> annotation = classForName(ANNOTATION_PACKAGE + "." + method.getName().replaceAll(VALIDATION_METHOD_PREFIX, ""));
					doWithFields(obj, annotation, lista, new FieldValidator() {
						public void validate(Field field, Object obj, Annotation annotation, ListaMensagens lista) {
							if (!fieldWithErrors.contains(field)) {
								int size = lista.size();
								ReflectionUtils.invokeMethod(method, obj, new Object[] { field, obj, annotation, lista });

								if (lista.size() > size)
									fieldWithErrors.add(field);
							}
						}
					});
				}
			}
		});
	}
	
	/**
	 * Valida um objeto para a anota��o passada como par�metro.
	 * @param obj
	 * @param lista
	 */
	public static void validate(final Object obj, final Class<?> annotation, final ListaMensagens lista) {
		final Method method = getMethod(AnnotationValidation.class, VALIDATION_METHOD_PREFIX + annotation.getSimpleName());
		final List<Field> fieldWithErrors = new ArrayList<Field>();
		
		doWithFields(obj, annotation, lista, new FieldValidator() {
			public void validate(Field field, Object obj, Annotation annotation, ListaMensagens lista) {
				if (!fieldWithErrors.contains(field)) {
					int size = lista.size();
					ReflectionUtils.invokeMethod(method, obj, new Object[] { field, obj, annotation, lista });

					if (lista.size() > size)
						fieldWithErrors.add(field);
				}
			}
		});
	}

	protected static void supportedTypes(Field field, Class<?>... classes) {
		boolean supported = false;
		
		for (Class<?> clazz : classes) {
			if (field.getType().isAssignableFrom(clazz)) {
				supported = true;
				break;
			}
		}
		
		if (!supported)
			throw new ValidationException("O campo " + field.getName() + " n�o est� entre os tipos suportados pela valida��o. Os tipos "
					+ "suportados s�o: " + StringUtils.transformaEmLista(CollectionUtils.arrayToList(classes)));
	}
	
	// Itera sobre os atributos que possuem a anota��o especificada e aplica valida��es.
	private static void doWithFields(Object obj, Class<?> annotation, ListaMensagens lista, FieldValidator validator) {
		List<Field> fields = findFieldsWithAnnotation(obj.getClass(), annotation);
		if (!isEmpty(fields)) {
			for (Field field : fields) {
				Annotation ann = getAnnotation(field, annotation);
				validator.validate(field, obj, ann, lista);
			}
		}
	}
	
	protected interface FieldValidator {
		public void validate(Field field, Object obj, Annotation annotation, ListaMensagens lista);
	}
	
}
