package de.test.automatedTests.actionsMenuTests;

import de.test.automatedTests.managers.ApplicationManager;
import de.test.automatedTests.managers.ProformaManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
import java.util.List;

import static de.test.automatedTests.managers.ProformaManager.ARROW_SELECTOR;
import static de.test.automatedTests.managers.ProformaManager.HEADERS_LIST_SELECTOR;

public class ProformaAcceptanceTests extends AbstractProformaTest {

    /**
     * if will find in the comment below the structure "second tabel " this is used for describe the babek that show after pressing the extend row of the main tabel
     **/
    @Test
    public void proformaAcceptanceTests() {

        SoftAssert softAssert = new SoftAssert();
        List<WebElement> pageArrows;
        List<WebElement> headerRow;
        List<WebElement> rowsDetails;
        String noHeader;
        String typeOf;

        //login on the page
        ApplicationManager.loginOnPage("admin", "654321", getWebDriver());
        //enter on the page proforma page
        homePageMananger.goToPraformaPage("Actions", "Proforma Invoices");
        //select max number of item on the page at 200
        proformaManager.selectNumberOfElementsOnPage();
        //return to the header of the tabel
        new Actions(getWebDriver()).moveToElement(getWebDriver().findElement(By.cssSelector(ProformaManager.PAGE_TITLE))).perform();
        List<WebElement> headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));

        for (int j = 0; j < headersList.size(); j++) {

            //identify all right arrow from page that will expend the second tabel
            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));

            //focus on the arrow
            new Actions(getWebDriver()).moveToElement(pageArrows.get(j)).perform();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOf(pageArrows.get(j)));
            //identify all the row of the main tabel
            headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));

            //copy all the data from the j row on the headerRow
            headerRow = headersList.get(j).findElements(By.cssSelector("td"));

            //save the Invoice of the current element
            noHeader = headerRow.get(1).getText();

            // save the unit type "pieces" or "m3"
            typeOf = headerRow.get(8).getText();

            //extend the detailed tabel for the j row
            pageArrows.get(j).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            //identify all the rows from the extended tabel
            rowsDetails = proformaManager.getDetailTabel();

            // verify the all the rows of the second tabel
            for (int i = 0; i < rowsDetails.size(); i++) {
                //copy in the rowData object the dates from the "i" row of the second tabel
                ProformaManager.OrderData rowData = proformaManager.saveDataFromRow(i, rowsDetails);

                softAssert.assertTrue(verifyLinealMeters(rowData), "For no " + noHeader + " in table at index = " + i + " lineal meters have a bad value eg:" + rowData.fixed * rowData.count + "!=" + rowData.linearMeters);
                softAssert.assertTrue(verifyPackageVolume(rowData), "For no " + noHeader + " in table at index = " + i + " m3/packets have a bad value eg :" + rowData.width * rowData.thickness * rowData.fixed * rowData.count / 1000000 + "!=" + rowData.m3packets);
                softAssert.assertTrue(verifyPackageArea(rowData), "For no " + noHeader + " in table at index = " + i + " area " + rowData.linearMeters * rowData.width / 1000 + "!=" + rowData.m2packets);
                softAssert.assertTrue(verifyTotalM3(rowData), "For no " + noHeader + " in table at index = " + i + " total volume: " + rowData.m3packets * rowData.packets + "!= " + rowData.totalM3);
                softAssert.assertTrue(verifyTotalPriceFromPricePacket(rowData), "For no " + noHeader + " in table at index = " + i + " the total price calculate from package price: " + rowData.width * rowData.thickness * rowData.fixed * rowData.count * rowData.packets * rowData.priceM3 / 1000000 + "!=" + rowData.total);

                //the formula for calculated total price is different for m3 to pieces
                if (!typeOf.equals("pieces"))
                    softAssert.assertTrue(verifyTotalPriceFromM3(rowData), "For no " + noHeader + " in table at index = " + i + " price calculate from volume price :" + rowData.width * rowData.thickness * rowData.fixed * rowData.count * rowData.packets * rowData.priceM3 / 1000000 + " != " + rowData.total);
                else
                    softAssert.assertTrue(verifyTotalPriceFromPiecesPrice(rowData), "For no " + noHeader + " in table at index = " + i + " price calculate from pieces price :" + rowData.fixed * rowData.priceM3 + rowData.count + " != " + rowData.total);
            }

            //verify the footer of the second tabel
            ProformaManager.OrderFinalData orderFinalData = proformaManager.saveDataFromTableFooter();
            softAssert.assertTrue(verifyTotalCount(orderFinalData, rowsDetails), "For no " + noHeader + " Total number from footer is wrong " + orderFinalData.totalCount);
            softAssert.assertTrue(verifyTotalLinearMeters(orderFinalData, rowsDetails), "For no " + noHeader + " Total lineal meters is bad " + orderFinalData.totalLinearMeters);
            softAssert.assertTrue(verifyTotalPackets(orderFinalData, rowsDetails), "For no " + noHeader + " Total packets from footer is wrong" + orderFinalData.totalPackets);
            softAssert.assertTrue(verifyTotalM3Footer(orderFinalData, rowsDetails), "For no " + noHeader + " Total volume from footer is wrong " + orderFinalData.totalM3Final);
            softAssert.assertTrue(verifyTotalPrice(orderFinalData, rowsDetails), "For no " + noHeader + " Total prise from footer is wrong " + orderFinalData.totalMoney);

            //close the detailed tabel
            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));
            pageArrows.get(j).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            //identify again the header / row "j" of the main tabel and verify total money and the total packets number
            headersList = getWebDriver().findElements(By.cssSelector(HEADERS_LIST_SELECTOR));
            headerRow = headersList.get(j).findElements(By.cssSelector("td"));
            softAssert.assertTrue(verifyHeaderTotalPrice(headerRow.get(5), orderFinalData.totalMoney), "For no " + noHeader + " total money 1 is wrong " + orderFinalData.totalMoney);
            softAssert.assertTrue(verifyHeaderTotalPrice(headerRow.get(6), orderFinalData.totalMoney), "For no " + noHeader + " total money 2 is wrong " + orderFinalData.totalMoney);
            softAssert.assertTrue(verifyPacketsCountFromHeader(headerRow.get(9), orderFinalData.totalPackets), "For no " + noHeader + " total packets is wrong " + headerRow.get(9).getText() + " != " + orderFinalData.totalPackets);

        }
        //print all the errors
        softAssert.assertAll();
    }


    @Test
    public void verifyTotalMoneyOnTheMainPAge() {
        List<WebElement> headerRow;
        List<WebElement> elements;
        Float totalAmountUsd = Float.valueOf(0);
        Float totalAmountNzd = Float.valueOf(0);
        Float totalUsd;
        Float totalNzd;

        ApplicationManager.loginOnPage("admin", "654321", getWebDriver());
        //enter on the page proforma page
        homePageMananger.goToPraformaPage("Actions", "Proforma Invoices");
        //select max number of item on the page at 200
        proformaManager.selectNumberOfElementsOnPage();
        //return to the header of the tabel
        new Actions(getWebDriver()).moveToElement(getWebDriver().findElement(By.cssSelector(ProformaManager.PAGE_TITLE))).perform();

        List<WebElement> headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));

        for (int i = 0; i < headersList.size(); i++) {
            headersList = getWebDriver().findElements(By.cssSelector(ProformaManager.HEADERS_LIST_SELECTOR));

            //copy all the data from the j row on the headerRow
            headerRow = headersList.get(i).findElements(By.cssSelector("td"));
            
            if (headerRow.get(4).getText().equals("NZD"))
                totalAmountNzd += Float.parseFloat(headerRow.get(5).getText().replaceAll("[^\\d.]", ""));
            else
                totalAmountUsd += Float.parseFloat(headerRow.get(5).getText().replaceAll("[^\\d.]", ""));
        }
        // new Actions(getWebDriver()).moveToElement(getWebDriver().findElement(By.cssSelector(ProformaManager.PAGE_TITLE))).perform();
        List<WebElement> footerRows = getWebDriver().findElements(By.cssSelector(".rgFooter"));

        elements = footerRows.get(1).findElements(By.cssSelector("td"));
        totalNzd = Float.parseFloat(elements.get(5).getText().replaceAll("[^\\d.]", ""));
        elements = footerRows.get(2).findElements(By.cssSelector("td"));
        totalUsd = Float.parseFloat(elements.get(5).getText().replaceAll("[^\\d.]", ""));

        Assert.assertEquals(totalNzd, totalAmountNzd, "total amount NZD not match");
        Assert.assertEquals(totalUsd, totalAmountUsd, "total amount NZD not match");

    }


    //verify linear meters that is calculate like a multiplication between fixed and count
    public boolean verifyLinealMeters(ProformaManager.OrderData rowData) {
        Float aux = rowData.fixed * rowData.count;
        aux = Float.parseFloat(proformaManager.decimalFormatExtended.format(aux));
        return aux == rowData.linearMeters;
    }

    //verify the volume with formula width*thickness*count*fixed and divided with 1000000 to obtain cube feet
    public boolean verifyPackageVolume(ProformaManager.OrderData rowData) {

        float aux = rowData.width * rowData.thickness * rowData.count / 1000000 * rowData.fixed;
        aux = Float.parseFloat(proformaManager.decimalFormatExtended.format(aux));
        rowData.m3packets = Float.parseFloat(proformaManager.decimalFormatExtended.format(rowData.m3packets));
        return aux == rowData.m3packets;
    }

    //verify the total area from linearMeters*width and divide with 10000 for  square feet
    public boolean verifyPackageArea(ProformaManager.OrderData rowData) {

        float aux = rowData.linearMeters * rowData.width / 1000;
        aux = BigDecimal.valueOf(aux).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        aux = Float.parseFloat(proformaManager.decimalFormatShort.format(aux));
        return aux == rowData.m2packets;
    }

    //verify total m3 by multiply the volume of a packet with the number of packets
    public boolean verifyTotalM3(ProformaManager.OrderData rowData) {

        float aux = rowData.m3packets * rowData.packets;
        aux = BigDecimal.valueOf(aux).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
        return aux == rowData.totalM3;
    }

    //verify the total price from the first method by calculate total volume and multiply with price/packet
    public boolean verifyTotalPriceFromM3(ProformaManager.OrderData rowData) {

        float aux = rowData.width * rowData.thickness * rowData.fixed * rowData.count * rowData.packets * rowData.priceM3 / 1000000;
        aux = Float.parseFloat(proformaManager.decimalFormatShort.format(aux));
        return aux == rowData.total;
    }

    public boolean verifyTotalPriceFromPiecesPrice(ProformaManager.OrderData rowData) {

        float aux = rowData.priceM3 * rowData.count * rowData.packets;
        aux = Float.parseFloat(proformaManager.decimalFormatShort.format(aux));
        return aux == rowData.total;
    }

    //verify total price  from second method by multiply price/packet with number of packets
    public boolean verifyTotalPriceFromPricePacket(ProformaManager.OrderData rowData) {

        float aux = rowData.pricePacket * rowData.packets;
        aux = BigDecimal.valueOf(aux).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return aux == rowData.total;
    }

    //verify in the footer of the second tabel the total counts
    public boolean verifyTotalCount(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        int totalCount = 0;
        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).count;
        return totalCount == orderFinalData.totalCount;
    }

    //verify in the footer of the second tabel the total linear meters
    public boolean verifyTotalLinearMeters(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;
        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).linearMeters;
        return totalCount == orderFinalData.totalLinearMeters;
    }

    //verify in the footer of the second tabel the total packets number
    public boolean verifyTotalPackets(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;
        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).packets;

        return totalCount == orderFinalData.totalPackets;
    }

    //verify in the footer of the second tabel the total volume of the order
    public boolean verifyTotalM3Footer(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;
        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).totalM3;
        totalCount = Float.parseFloat(proformaManager.decimalFormatExtended.format(totalCount));
        return totalCount == orderFinalData.totalM3Final;
    }

    //verify in the footer of the second tabel the total price of the order
    public boolean verifyTotalPrice(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).total;

        //totalCount = Float.parseFloat(proformaManager.decimalFormatShort.format(totalCount));
        totalCount= (float) (Math.floor(totalCount * 1e2) / 1e2);
        return totalCount == orderFinalData.totalMoney;
    }

    //verify it the total price from the header of the page is the same with the amount of the all rows of the second tabel
    private boolean verifyHeaderTotalPrice(WebElement headerValue, float totalMoney) {
        Float total = Float.parseFloat(headerValue.getText().replaceAll("[^\\d.]", ""));
        total = Float.parseFloat(proformaManager.decimalFormatShort.format(total));
        totalMoney = Float.parseFloat(proformaManager.decimalFormatShort.format(totalMoney));

        return total == totalMoney;
    }

    //verify it the total count from the header of the page is the same with the amount of the all counts of the second tabel
    private boolean verifyPacketsCountFromHeader(WebElement headerCount, float countTotal) {
        Integer count = Integer.parseInt(headerCount.getText());
        return count == countTotal;
    }
}
