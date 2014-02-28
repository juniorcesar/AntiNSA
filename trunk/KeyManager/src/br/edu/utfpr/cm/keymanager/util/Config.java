package br.edu.utfpr.cm.keymanager.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.edu.utfpr.cm.keymanager.activity.ConfigActivity;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

public class Config {
	public static final String CONFIG_FILE = "keymanager.xml";

	public static boolean readConfigFile(FileInputStream fis, byte[] bytes) {

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			String line = null, input = "";
			while ((line = reader.readLine()) != null) {
				input += line;
			}
			reader.close();
			fis.close();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void writeConfigFile(FileOutputStream fos, byte[] bytes) {
		try {
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String converter(Document document)
			throws TransformerException {
		// Criamos uma instancia de Transformer
		// O método setOutputProperty cria a formatação
		// ou não do XML no arquivo. Se 'yes' então formata,
		// se 'no' então escreve o arquivo em uma única linha
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		// inicializar StreamResult para gravar para String
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(document);
		transformer.transform(source, result);

		// Recupera o conteúdo em formato String
		String xmlString = result.getWriter().toString();

		Log.v("MSG", xmlString);

		// devolve a string
		return xmlString;
	}

	public void generateXml(FileOutputStream fos) throws Exception {
		// Criamos um nova instancia de DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// Usamos o método newDocumentBuilder() para
		// criar um nova instancia de DocumentBuilder
		DocumentBuilder db = dbf.newDocumentBuilder();
		// Obtém uma nova inst ncia de um Document
		// objeto para construir uma árvore com Document.
		Document doc = db.newDocument();

		// Cria a tag raiz do XML que será Contato
		Element root = doc.createElement("keymanager");
		Element config = doc.createElement("config");
		Element port = doc.createElement("port");
		port.setTextContent("15000");
		Element passwd = doc.createElement("passwd");
		Element keys = doc.createElement("keys");
		root.appendChild(config);
		config.appendChild(port);
		config.appendChild(passwd);
		root.appendChild(keys);

		doc.appendChild(root);

		String file = converter(doc);
		writeConfigFile(fos, file.getBytes());
	}

	public static void setTagValue(FileInputStream fis, FileOutputStream fos, String tagName, String value)
			throws Exception {
		Document doc = getDocument(fis);
		Element root =  doc.getDocumentElement();
		NodeList children = root.getElementsByTagName(tagName);
		String result = null;
		// children é a tag pai que estamos lendo,
		// por exemplo a tag <Endereco>
		// child é a tag que queremos recuperar o valor, por exemplo
		// a tag <cidade>
		Element child = (Element) children.item(0);
		if (child != null) {
			child.setTextContent(value);	
		}
		writeConfigFile(fos, converter(doc).getBytes());
		
		
	}

	public static String getTagValue(FileInputStream fis, String tagName)
			throws Exception {
		Element element  = getDocument(fis).getDocumentElement();
		NodeList children = element.getElementsByTagName(tagName);
		String result = null;
		// children é a tag pai que estamos lendo,
		// por exemplo a tag <Endereco>
		if (children == null) {
			return result;
		}
		// child é a tag que queremos recuperar o valor, por exemplo
		// a tag <cidade>
		Element child = (Element) children.item(0);
		if (child == null) {
			return result;
		}
		// recuperamos o texto contido na tagName  
		result = child.getTextContent();
		return result;
	}

	private static Document getDocument(FileInputStream fis) throws Exception, SAXException,
			TransformerException {
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 DocumentBuilder db = dbf.newDocumentBuilder();
		// //Criamos um objeto Element que receberá as informações de doc
		 Document doc = db.parse(fis);
		 fis.close();
		 return doc;
		//
		// //Vamos criar um objeto Endereco
		// Endereco endereco = new Endereco();
		// //Informamos qual tag vamos ler
		// NodeList endList = raiz.getElementsByTagName("Endereco");
		// Element endElement = (Element) endList.item(0);
		// //Passamos para o método getChildTagValue a tag Pai que
		// //iremos ler mais qual tag Filha será capturada
		// endereco.setId(Integer.parseInt(getChildTagValue(endElement, "id")));
		// endereco.setNumero(Integer.parseInt(getChildTagValue(endElement,
		// "numero")));
		// endereco.setBairro(getChildTagValue(endElement, "bairro"));
		// endereco.setCep(getChildTagValue(endElement, "cep"));
		// endereco.setCidade(getChildTagValue(endElement, "cidade"));
		// endereco.setComplemento(getChildTagValue(endElement, "complemento"));
		// endereco.setLogradouro(getChildTagValue(endElement, "logradouro"));
		//
		// //Vamos criar uma coleção de Telefones já que
		// // temos mais de um Telefone a capturar
		// Collection<Telefone> telefones = new ArrayList<Telefone>();
		// NodeList fonesList = raiz.getElementsByTagName("Telefone");
		// Element foneElement;
		// //Fazemos um for nas tags Telefone e adicionamos os dados
		// // em um objeto Telefone e depois na coleção
		// for (int i = 0; i < fonesList.getLength(); i++) {
		// foneElement = (Element) fonesList.item(i);
		// Telefone telefone = new Telefone();
		// telefone.setId(Integer.parseInt(getChildTagValue(foneElement,
		// "id")));
		// telefone.setDdd(Integer.parseInt(getChildTagValue(foneElement,
		// "ddd")));
		// telefone.setNumero(Integer.parseInt(getChildTagValue(foneElement,
		// "numero")));
		// //Adicionamos a coleção as tags Telefone
		// telefones.add(telefone);
		// }
		//
		// //Agora iremos ler os dados de ContatosalvarArquivo
		// //Como esses dados estão apenas dentro
		// // da tag Contato e de mais nenhuma outra
		// // vamos então usar o elemento raiz
		// Contato contato = new Contato();
		// contato.setId(Integer.parseInt(getChildTagValue(raiz, "id")));
		// contato.setNome(getChildTagValue(raiz, "nome"));
		// contato.setEmail(getChildTagValue(raiz, "email"));
		//
		// //Agora vamos inserir em contato os objetos telefones e endereco
		// contato.setEndereco(endereco);
		// contato.setTelefones(telefones);
		//
		// return contato;
	}
}
