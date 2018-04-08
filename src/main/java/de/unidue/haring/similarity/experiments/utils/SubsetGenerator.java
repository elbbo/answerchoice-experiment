package de.unidue.haring.similarity.experiments.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import de.unidue.haring.similarity.experiments.types.Instance;
import de.unidue.haring.similarity.experiments.types.QuestionAnswerProblem;

public class SubsetGenerator
{
    private static final String FILE_PATH = "src/test/resources/data/";
    private static List<Instance> iList;

    // True indicates QuestionAnswerProblem shell be filtered by question, otherwise filtered by
    // answer
    private static final boolean FILTER = true;

    public static void main(String args[])
    {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            InstanceHandler iHandler = new InstanceHandler();

            saxParser.parse(new File("src/test/resources/data/test-data.xml"), iHandler);
            iList = iHandler.getInstanceList();

            String[] keywords = {"who", "whose" };
            writeXmlFile(FILTER, iList, keywords);
        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void writeXmlFile(boolean filter, List<Instance> il, String... keywords)
    {
        try {
            boolean match = false;

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);

            for (Instance i : il) {
                int questionCount = 0;
                Element instance = doc.createElement("instance");
                Attr instanceId = doc.createAttribute("id");
                instanceId.setValue(String.valueOf(i.getInstanceId()));
                Attr instanceScenario = doc.createAttribute("scenario");
                instanceScenario.setValue(i.getInstanceScenario());
                instance.setAttributeNode(instanceId);
                instance.setAttributeNode(instanceScenario);

                Element text = doc.createElement("text");
                text.appendChild(doc.createTextNode(i.getInstanceText()));
                instance.appendChild(text);

                Element questions = doc.createElement("questions");
                for (QuestionAnswerProblem qp : i.getqList()) {
                    // handle question type
                    match = false;
                    
                    if (filter) {
                        for (String keyword : keywords) {
                            if (qp.getQuestionText().toLowerCase().startsWith(keyword))
                                match = true;
                        }
                    }
                    else if (!filter) {
                        for (String keyword : keywords) {
                            if (qp.getAnswerText1().contains(keyword)
                                    || qp.getAnswerText2().toLowerCase().contains(keyword)) {
                                match = true;
                            }
                        }
                    }
                    if (!match) {
                        continue;
                    }

                    Element question = doc.createElement("question");

                    Attr q_text_attr = doc.createAttribute("text");
                    q_text_attr.setValue(qp.getQuestionText());
                    Attr cs_attr = doc.createAttribute("type");
                    cs_attr.setValue(qp.getQuestionType());
                    Attr id_attr = doc.createAttribute("id");
                    id_attr.setValue(String.valueOf(qp.getQuestionId()));
                    question.setAttributeNode(q_text_attr);
                    question.setAttributeNode(cs_attr);
                    question.setAttributeNode(id_attr);

                    Element answer1 = doc.createElement("answer");
                    Attr a1_text_attr = doc.createAttribute("text");
                    a1_text_attr.setValue(qp.getAnswerText1());
                    Attr a1_id_attr = doc.createAttribute("id");
                    a1_id_attr.setValue("0");
                    Attr a1_correct_answer_attr = doc.createAttribute("correct");
                    if (qp.getIDCorrectAnswer() == 0) {
                        a1_correct_answer_attr.setValue("True");
                    }
                    else {
                        a1_correct_answer_attr.setValue("False");
                    }
                    answer1.setAttributeNode(a1_text_attr);
                    answer1.setAttributeNode(a1_id_attr);
                    answer1.setAttributeNode(a1_correct_answer_attr);

                    Element answer2 = doc.createElement("answer");
                    Attr a2_text_attr = doc.createAttribute("text");
                    a2_text_attr.setValue(qp.getAnswerText2());
                    Attr a2_id_attr = doc.createAttribute("id");
                    a2_id_attr.setValue("1");
                    Attr a2_correct_answer_attr = doc.createAttribute("correct");
                    if (qp.getIDCorrectAnswer() == 1) {
                        a2_correct_answer_attr.setValue("True");
                    }
                    else {
                        a2_correct_answer_attr.setValue("False");
                    }

                    answer2.setAttributeNode(a2_text_attr);
                    answer2.setAttributeNode(a2_id_attr);
                    answer2.setAttributeNode(a2_correct_answer_attr);

                    question.appendChild(answer1);
                    question.appendChild(answer2);

                    questions.appendChild(question);
                    questionCount++;
                }
                if (questionCount > 0) {
                    instance.appendChild(questions);
                    rootElement.appendChild(instance);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            String fileName = "testdata_subset";
            for (String keyword : keywords) {
                fileName += "_" + keyword;
            }
            StreamResult result = new StreamResult(new File(FILE_PATH + fileName + ".xml"));

            transformer.transform(source, result);

            System.out.println("File saved!");

        }
        catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}
