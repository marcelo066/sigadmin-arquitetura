/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 11/11/2008
 */
package br.ufrn.arq.util;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe com m�todos utilit�rios para trabalhar com o FacesContext do JSF.
 * 
 * @author Leonardo Campos
 * @author Gleydson Lima
 *
 */
public class FacesContextUtils {

	 // Getters -----------------------------------------------------------------------------------

    public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
        // Get current FacesContext.
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Check current FacesContext.
        if (facesContext == null) {

            // Create new Lifecycle.
            LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

            // Create new FacesContext.
            FacesContextFactory contextFactory  = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

            // Create new View.
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
            facesContext.setViewRoot(view);                

            // Set current FacesContext.
            FacesContextWrapper.setCurrentInstance(facesContext);
        }

        return facesContext;
    }

    // Helpers -----------------------------------------------------------------------------------

    // Wrap the protected FacesContext.setCurrentInstance() in a inner class.
    private static abstract class FacesContextWrapper extends FacesContext {
        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    } 
}
