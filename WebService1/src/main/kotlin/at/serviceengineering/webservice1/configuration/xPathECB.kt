package at.serviceengineering.webservice1.configuration

import org.springframework.stereotype.Service
import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.*

// https://stackoverflow.com/questions/50316974/how-to-read-an-online-xml-file-for-currency-rates-in-java
@Service
class xPathECB {
    private val currency = "currency"
    private val cubeNode = "//Cube/Cube/Cube"
    private val rate = "rate"

    fun getRates(): MutableList<CurrencyRate> {
        val currRateList: MutableList<CurrencyRate> = ArrayList<CurrencyRate>()
        val builderFactory = DocumentBuilderFactory.newInstance()
        var builder: DocumentBuilder? = null
        try {
            builder = builderFactory.newDocumentBuilder()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }
        var document: Document? = null
        val spec = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml"
        try {
            val url = URL(spec)
            val stream: InputStream = url.openStream()
            document = builder!!.parse(stream)
            val xPathfactory: XPathFactory = XPathFactory.newInstance()
            val xpath: XPath = xPathfactory.newXPath()
            val xPathString = cubeNode
            val expr: XPathExpression = xpath.compile(xPathString)
            val nl: NodeList = expr.evaluate(document, XPathConstants.NODESET) as NodeList
            for (i in 0 until nl.length) {
                val node: Node = nl.item(i)
                val attribs: NamedNodeMap = node.attributes
                if (attribs.length > 0) {
                    val currencyAttrib: Node? = attribs.getNamedItem(currency)
                    if (currencyAttrib != null) {
                        val currencyTxt: String = currencyAttrib.nodeValue
                        val rateTxt = attribs.getNamedItem(rate).nodeValue
                        currRateList.add(CurrencyRate(currencyTxt, rateTxt))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XPathExpressionException) {
            e.printStackTrace()
        }
        return currRateList
    }
}

class CurrencyRate(val currency: String,
                   val rate: String) {

    override fun toString(): String {
        return "CurrencyRate [currency=$currency, rate=$rate]"
    }
}
