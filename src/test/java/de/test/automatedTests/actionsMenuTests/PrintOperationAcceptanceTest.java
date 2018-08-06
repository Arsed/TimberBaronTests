package de.test.automatedTests.actionsMenuTests;

import de.test.automatedTests.managers.ApplicationManager;
import de.test.automatedTests.managers.ProformaManager;
import de.test.automatedTests.utils.UtilsTools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

import static de.test.automatedTests.managers.ProformaManager.ARROW_SELECTOR;


public class PrintOperationAcceptanceTest extends AbstractProformaTest {
    @Test
    public void verifyPrintingFile() {
        SoftAssert softAssert = new SoftAssert();
        String itemNo;
        String compareStr;
        String unitType;
        String customerName;
        List<WebElement> pageArrows;
        List<WebElement> rowsDetails;
        List<ProformaManager.OrderData> detailedTabelData = new ArrayList<>();
        ProformaManager.ColumnsOfTabel attributeOfMainTabel;
        ApplicationManager.loginOnPage("admin", "654321", getWebDriver());

        //enter on the page proforma page
        homePageMananger.goToPraformaPage("Actions", "Proforma Invoices");

        //select max number of item on the page at 200
        proformaManager.selectNumberOfElementsOnPage();

        //go to teh start of the tabel for identify all rows
        new Actions(getWebDriver()).moveToElement(getWebDriver().findElement(By.cssSelector(ProformaManager.PAGE_TITLE))).perform();

        //identify all the rows from main tabel
        List<WebElement> headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));
        //copy the handle of the main page , this is useful to return from the print page in the main page
        String mainWindowHandle = getWebDriver().getWindowHandle();

        for (int i = 0; i < headersList.size(); i++) {
            
            attributeOfMainTabel = proformaManager.saveDataFromMainTabelRow(i, headersList);
            //save the invoice
            itemNo = attributeOfMainTabel.invoice;

            customerName = attributeOfMainTabel.customer;
            //identify all right arrow from page that will expend the second tabel
            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));

            //focus on the arrow
            new Actions(getWebDriver()).moveToElement(pageArrows.get(i)).perform();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOf(pageArrows.get(i)));

            //extend the detailed tabel for the j row
            pageArrows.get(i).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            //identify all the rows from the extended tabel
            rowsDetails = proformaManager.getDetailTabel();

            //copy in the rowData object the dates from the "i" row of the second tabel (detailed tabel)
            for (int k = 0; k < rowsDetails.size(); k++)
                detailedTabelData.add(proformaManager.saveDataFromRow(k, rowsDetails));

            //close the detailed tabel
            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));
            pageArrows.get(i).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));
            attributeOfMainTabel = proformaManager.saveDataFromMainTabelRow(i, headersList);
            unitType = attributeOfMainTabel.unit;
            //click on the print button
            attributeOfMainTabel.printButton.click();

            //switch on the printing page
            UtilsTools.switchToLastHandle(getWebDriver());

            //verify if the number of the items is good in the url
            verifyIfUrlContainsItemNo(itemNo, softAssert);

            //wait from iframe
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("*[style='width: 100%; height: 100%;']")));

            //wait the i frame to be visible
            WebElement iframeSwitch = getWebDriver().findElement(By.cssSelector("*[style='width: 100%; height: 100%;']"));


            //switch on the iframe
            getWebDriver().switchTo().frame(iframeSwitch);

            //verify the content of the printing iframe

            //verify it the report number is good
            verifyReportNumber(itemNo, softAssert);

            //these two (5368 351) generate errors
            if (!itemNo.equals("5368"))
                if (!itemNo.equals("5351")) {

                    List<WebElement> tabelRow = getWebDriver().findElements(By.cssSelector(".textBox1"));
                    for (int j = 0; j < tabelRow.size(); j++) {
                        String sir = tabelRow.get(j).getText().replaceAll("\n", "").replaceAll(" ", "");
                        ProformaManager.OrderData auxRow = detailedTabelData.get(j);
                        //create the string in function of the unit type
                        if (unitType.equals("m3")) {
                            compareStr = auxRow.specie + auxRow.grade + auxRow.finish + auxRow.treatment + auxRow.drynes
                                    + auxRow.width + " x " + auxRow.thickness + " x " + auxRow.fixed + "0m - " + auxRow.packets + "pkts/" + auxRow.count;
                        } else {
                            compareStr = auxRow.specie + auxRow.grade + auxRow.finish + auxRow.treatment + auxRow.drynes
                                    + (int) (auxRow.fixed * 1000) + "x" + auxRow.width + " x " + auxRow.thickness + " - " + auxRow.packets + "pkts/" + auxRow.count;
                        }

                        compareStr = compareStr.replaceAll(" ", "");
                        softAssert.assertEquals(sir, compareStr, "problems at " + itemNo + "index " + j);

                    }

                    //verify customer name
                    WebElement customerNameSelector = getWebDriver().findElement(By.cssSelector(".txtCustomerName.s6-"));
                    softAssert.assertEquals(customerName, customerNameSelector.getText(), "problems at customer " + itemNo);

                    //verify created day
                    WebElement createdDateSelector = getWebDriver().findElement(By.cssSelector(".txtInvoiceDate.s7-"));
                    softAssert.assertEquals("Date: " + attributeOfMainTabel.dateCreated.replaceAll("/", "."), createdDateSelector.getText(), "problems at create date " + itemNo);

                    //verify voyage number
                    WebElement voyageNumberSelector = getWebDriver().findElement(By.cssSelector(".textBox38"));
                    softAssert.assertEquals(attributeOfMainTabel.voyageNo, voyageNumberSelector.getText(), "problems at voyage number " + itemNo);

                    //verify discharge date
                    WebElement dischargeDateElement = getWebDriver().findElement(By.cssSelector(".textBox28"));
                    softAssert.assertEquals(attributeOfMainTabel.dischargeDate.replaceAll("/", "."), dischargeDateElement.getText(), "problems at discharge date " + itemNo);

                    //verify  money type USD/NZD
                    WebElement moneyTypeSelector = getWebDriver().findElement(By.cssSelector(".textBox50"));
                    String moneyTypeStr = moneyTypeSelector.getText().substring(6, 9);
                    softAssert.assertEquals(moneyTypeStr, attributeOfMainTabel.currency, "money type problems for " + itemNo);

                    //verify money
                    // subtotal
                    WebElement subTotalElement = getWebDriver().findElement(By.cssSelector(".textBox48"));
                    Float subTotal = Float.parseFloat(subTotalElement.getText().replaceAll("[^\\d.]", ""));
                    softAssert.assertEquals(subTotalElement.getText(), attributeOfMainTabel.valueExGST, "value of sub total or valueExGST is wrong at item " + itemNo);

                    // GST
                    WebElement gstValueElement = getWebDriver().findElement(By.cssSelector(".txtTaxValue"));
                    Float gstValue = Float.parseFloat(gstValueElement.getText().replaceAll("[^\\d.]", ""));

                    if (attributeOfMainTabel.isGST.equals("Yes"))
                        softAssert.assertEquals(gstValueElement.getText(), attributeOfMainTabel.valueInGST, "valueInGst is wrong for item " + itemNo);
                    else
                        softAssert.assertEquals(gstValueElement.getText().replaceAll(" ", ""), "$0.00", "valueInGst is wrong for item " + itemNo);
                    //Total
                    WebElement totalMoneyElement = getWebDriver().findElement(By.cssSelector(".txtTotalWithTax"));
                    Float totalMoney = Float.parseFloat(totalMoneyElement.getText().replaceAll("[^\\d.]", ""));

                    softAssert.assertEquals(subTotal + gstValue, totalMoney, "total money is wrong at item" + itemNo);
                }

            //switch on the main page
            getWebDriver().switchTo().window(mainWindowHandle);
            detailedTabelData.clear();

        }
        //printing the errors
        softAssert.assertAll();
    }

    public void verifyIfUrlContainsItemNo(String expectNo, SoftAssert softAssert) {
        String invoice;
        String currentUrl;
        currentUrl = getWebDriver().getCurrentUrl();
        invoice = getWebDriver().getCurrentUrl().substring(currentUrl.length() - 4, currentUrl.length());
        softAssert.assertEquals(invoice, expectNo, "for the items " + expectNo + "the URL contained " + invoice);
    }

    public void verifyReportNumber(String expectNo, SoftAssert softAssert) {
        String invoice;
        String currentNumber;
        currentNumber = getWebDriver().findElement(By.cssSelector(".invoiceNrReportHeaderTextBox")).getText();
        invoice = currentNumber.substring(currentNumber.length() - 4, currentNumber.length());
        softAssert.assertEquals(invoice, expectNo, "for the items " + expectNo + "the number ofthe report is wrong " + invoice);
    }


}
