package warstwaLogiki;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import warstwaDanych.Kontakt;
import warstwaDanych.Wydarzenia;

public class XML {
	public void zapisKontaktowDoXML(ArrayList<Kontakt> kontakt) {
		try {
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("./src/Kontakty.xml"), "utf-8");
			
			writer.writeStartDocument("1.0");
			
			writer.writeStartElement("ZapisaneKontakty");
			for (int i = 0; i < kontakt.size(); i++) {
				Kontakt aktualnyKontakt = kontakt.get(i);
				writer.writeStartElement("Kontakt");
				writer.writeStartElement("Imie");
				writer.writeCharacters(aktualnyKontakt.getImie());
				writer.writeEndElement();
				writer.writeStartElement("Nazwisko");
				writer.writeCharacters(aktualnyKontakt.getNazwisko());
				writer.writeEndElement();
				writer.writeStartElement("NumerTelefonu");
				writer.writeCharacters(Integer.toString(aktualnyKontakt.getNr()));
				writer.writeEndElement();
				writer.writeStartElement("Email");
				writer.writeCharacters(aktualnyKontakt.getEmail());
				writer.writeEndElement();
				for (int j = 0; j < kontakt.get(i).getWydarzeniaSize(); j++) {
					Wydarzenia aktualneWydarzenie = kontakt.get(i).getExactWydarzenie(j);
					writer.writeStartElement("Wydarzenie"+i);
					writer.writeStartElement("Nazwa");
					writer.writeCharacters(aktualneWydarzenie.getNazwa());
					writer.writeEndElement();
					writer.writeStartElement("Miejsce");
					writer.writeCharacters(aktualneWydarzenie.getMiejsce());
					writer.writeEndElement();
					writer.writeStartElement("Data");
					writer.writeCharacters(aktualneWydarzenie.getData());
					writer.writeEndElement();
					writer.writeStartElement("Godzina");
					writer.writeCharacters(aktualneWydarzenie.getGodzina());
					writer.writeEndElement();
					writer.writeEndElement();
				}writer.writeEndElement();
			}
			writer.writeEndElement();
			writer.flush();
			writer.close();
			System.out.println("Dane zapisane do pliku.");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public void odczytKontaktowXML(ArrayList<Kontakt> kontakt)
	{
		try {
			InputStream in = new FileInputStream("./src/Kontakty.xml");
				
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			try {
			    Document doc = dBuilder.parse(in);
			    doc.getDocumentElement().normalize();

			    NodeList nList = doc.getElementsByTagName("Kontakt");

			    for (int temp = 0; temp < nList.getLength(); temp++) {
			    	Node nNode = nList.item(temp);
			    	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			    		Element eElement = (Element) nNode;
			    		String imie = eElement.getElementsByTagName("Imie").item(0).getTextContent();
			    		String nazwisko = eElement.getElementsByTagName("Nazwisko").item(0).getTextContent();
			    		String nu = eElement.getElementsByTagName("NumerTelefonu").item(0).getTextContent();
			    		int numer = Integer.parseInt(nu);
			    		String email = eElement.getElementsByTagName("Email").item(0).getTextContent();
			    		Kontakt k = new Kontakt(imie, nazwisko, numer, email);
			    		kontakt.add(k);
			    		NodeList noList = doc.getElementsByTagName("Wydarzenie"+temp);
			            for (int i = 0; i < noList.getLength(); i++) {
			            	Node noNode = noList.item(i);
			            	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			            		Element elElement = (Element) noNode;
			            		String nazwa = elElement.getElementsByTagName("Nazwa").item(0).getTextContent();
					    		String miejsce = elElement.getElementsByTagName("Miejsce").item(0).getTextContent();
					    		String data = elElement.getElementsByTagName("Data").item(0).getTextContent();
					    		String godzina = elElement.getElementsByTagName("Godzina").item(0).getTextContent();
					    		Wydarzenia w = new Wydarzenia(nazwa, miejsce, data, godzina);
			            		kontakt.get(temp).setWydarzenie(w);
			            		w.setKontakt(k);
			            	}
			            };
			        }
			    }
			} catch(Exception e) {
				System.err.println("Pusty plik. Podaj dane");
			}		
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	public void zapisWydarzeniaDoXML(ArrayList<Wydarzenia> wydarzenia) {
		try {
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("./src/Wydarzenia.xml"), "utf-8");
			
			writer.writeStartDocument("1.0");
			
			writer.writeStartElement("ZapisaneWydarzenia");
			for (int i = 0; i < wydarzenia.size(); i++) {
				Wydarzenia aktualneWydarzenie = wydarzenia.get(i);
				writer.writeStartElement("Wydarzenie");
				writer.writeStartElement("Nazwa");
				writer.writeCharacters(aktualneWydarzenie.getNazwa());
				writer.writeEndElement();
				writer.writeStartElement("Miejsce");
				writer.writeCharacters(aktualneWydarzenie.getMiejsce());
				writer.writeEndElement();
				writer.writeStartElement("Data");
				writer.writeCharacters(aktualneWydarzenie.getData());
				writer.writeEndElement();
				writer.writeStartElement("Godzina");
				writer.writeCharacters(aktualneWydarzenie.getGodzina());
				writer.writeEndElement();
				writer.writeStartElement("ZapisaneKontakty");
				for (int j = 0; j < wydarzenia.get(i).getKontaktySize(); j++) {
					Kontakt aktualnyKontakt = wydarzenia.get(i).getExactKontakt(j);
					writer.writeStartElement("Kontakt"+i);
					writer.writeStartElement("Imie");
					writer.writeCharacters(aktualnyKontakt.getImie());
					writer.writeEndElement();
					writer.writeStartElement("Nazwisko");
					writer.writeCharacters(aktualnyKontakt.getNazwisko());
					writer.writeEndElement();
					writer.writeStartElement("NumerTelefonu");
					writer.writeCharacters(Integer.toString(aktualnyKontakt.getNr()));
					writer.writeEndElement();
					writer.writeStartElement("Email");
					writer.writeCharacters(aktualnyKontakt.getEmail());
					writer.writeEndElement();
					writer.writeEndElement();
				}
				writer.writeEndElement();
				writer.writeEndElement();
			}
			writer.writeEndElement();
			writer.flush();
			writer.close();
			System.out.println("Dane zapisane do pliku.");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void odczytWydarzenXML(ArrayList<Wydarzenia> wydarzenia)
	{
		try {
			InputStream in = new FileInputStream("./src/Wydarzenia.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    try {
		    Document doc = dBuilder.parse(in);
		    doc.getDocumentElement().normalize();
		    
		    NodeList nList = doc.getElementsByTagName("Wydarzenie");

		    for (int temp = 0; temp < nList.getLength(); temp++) {
		        Node nNode = nList.item(temp);
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		            Element eElement = (Element) nNode;
		            String nazwa = eElement.getElementsByTagName("Nazwa").item(0).getTextContent();
		            String miejsce = eElement.getElementsByTagName("Miejsce").item(0).getTextContent();
		            String data = eElement.getElementsByTagName("Data").item(0).getTextContent();
		            String godzina = eElement.getElementsByTagName("Godzina").item(0).getTextContent();
		            Wydarzenia w = (new Wydarzenia(nazwa, miejsce, data, godzina));
		            wydarzenia.add(w);
		            NodeList noList = doc.getElementsByTagName("Kontakt"+temp);
		            for (int i = 0; i < noList.getLength(); i++) {
		            	Node noNode = noList.item(i);
		            	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		            		Element elElement = (Element) noNode;
		            		String imie = elElement.getElementsByTagName("Imie").item(0).getTextContent();
		            		String nazwisko = elElement.getElementsByTagName("Nazwisko").item(0).getTextContent();
		            		String nu = elElement.getElementsByTagName("NumerTelefonu").item(0).getTextContent();
		            		int numer = Integer.parseInt(nu);
		            		String email = elElement.getElementsByTagName("Email").item(0).getTextContent();
		            		Kontakt k = new Kontakt(imie, nazwisko, numer, email);
		            		wydarzenia.get(temp).setKontakt(k);
		            		k.setWydarzenie(w);
		            	}
		            }
		        }
		    }
		    } catch(Exception e) {
		    	System.err.println("Pusty plik. Podaj dane");
		    }
		    
		    } catch (Exception e) {
		    e.printStackTrace();
		    }
		}
	}	

