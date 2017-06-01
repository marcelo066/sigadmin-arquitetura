/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 14/11/2007
 */
package br.ufrn.arq.web.captcha;

import java.awt.Color;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Classe para cria��o de imagens para serem utilizadas
 * como captchas.
 * 
 * @author Ricardo Wendell
 * @author Gleydson Lima
 *
 */
public class Captcha {

	private ImageCaptchaService service;

	private static Captcha instance = new Captcha();

	/* A classe deve ser um Singleton */

	private Captcha() {
		DefaultManageableImageCaptchaService serv = new DefaultManageableImageCaptchaService();
		serv.setCaptchaEngine(new EngineNumeros());
		service = serv;
	}

	public static Captcha getInstance() {
		return instance;
	}

	public ImageCaptchaService getService() {
		return service;
	}

	/* Especializando um Engine para gerar apenas n�meros */

	class EngineNumeros extends ListImageCaptchaEngine {

		@Override
		protected void buildInitialFactories() {

			// Gerador de caracteres, definido o n�mero de caracteres e a fonte
			TextPaster textPaster = new RandomTextPaster(6, 6, Color.white);
			
			// Defini��o do plano de fundo aleat�rio
			BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(150, 60);
			
			// Um gerador de fonte, � respons�vel por distorcer o texto, definido o tamanho da fonte
			FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(24, 28);

			/*
			 * O objeto respons�vel por juntar o background, a fonte e
			 * o texto para gerar a imagem
			 */
			WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);

			/*
			 * Adiciona o Factory RandomWordGenerator recebe os
			 * caracteres v�lidos, no caso queremos apenas n�meros
			 */
			this.addFactory(new GimpyFactory(new RandomWordGenerator ("0123456789"), wordToImage) );

		}

	}

}