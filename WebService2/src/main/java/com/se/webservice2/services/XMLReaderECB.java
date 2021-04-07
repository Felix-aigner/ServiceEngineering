package com.se.webservice2.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.se.webservice2.CurrencyRate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//content: https://stackoverflow.com/questions/50316974/how-to-read-an-online-xml-file-for-currency-rates-in-java

@Service
@Component
public class XMLReaderECB {

    private static final String CURRENCY = "currency";
    private static final String CUBE_NODE = "//Cube/Cube/Cube";
    private static final String RATE = "rate";

    public List<CurrencyRate> getRates() {
        List<CurrencyRate> currRateList = new ArrayList<>();
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        String spec = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        try {
            URL url = new URL(spec);
            InputStream is = url.openStream();
            document = builder.parse(is);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            String xPathString = CUBE_NODE;
            XPathExpression expr = xpath.compile(xPathString);
            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                NamedNodeMap attribs = node.getAttributes();
                if (attribs.getLength() > 0) {
                    Node currencyAttrib = attribs.getNamedItem(CURRENCY);
                    if (currencyAttrib != null) {
                        String currencyTxt = currencyAttrib.getNodeValue();
                        String rateTxt = attribs.getNamedItem(RATE).getNodeValue();
                        BigDecimal rate = new BigDecimal(rateTxt);
                        currRateList.add(new CurrencyRate(currencyTxt, rate));
                    }
                }
            }
        } catch (SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }

        for (CurrencyRate currencyRate : currRateList) {
            System.out.println(currencyRate);
        }

        return currRateList;

    }

}
