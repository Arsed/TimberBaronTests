package de.test.automatedTests.actionsMenuTests;

import de.test.automatedTests.managers.ApplicationManager;
import de.test.automatedTests.managers.ProformaManager;
import de.test.automatedTests.utils.UtilsTools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
import java.util.List;

import static de.test.automatedTests.managers.ProformaManager.ARROW_SELECTOR;
import static de.test.automatedTests.managers.ProformaManager.HEADERS_LIST_SELECTOR;

public class ProformaAcceptanceTests extends AbstractProformaTest {

    @Test
    public void proformaAcceptanceTests() {

        SoftAssert softAssert = new SoftAssert();

        //login on the page
        ApplicationManager.loginOnPage("admin", "654321", getWebDriver());
        //enter on the page that we need
        homePageMananger.goToPraformaPage("Actions", "Proforma Invoices");
        List<WebElement> pageArrows;
        List<WebElement> headerRow;

        proformaManager.selectNumberOfElementsOnPage();
        UtilsTools.scrollWitJavaScrip(0, -5000, getWebDriver());

        List<WebElement> headersList = getWebDriver().findElements(By.cssSelector(HEADERS_LIST_SELECTOR));

        for (int j = 0; j < headersList.size(); j++) {

            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));

            headersList = getWebDriver().findElements(By.cssSelector(HEADERS_LIST_SELECTOR));
            headerRow = headersList.get(j).findElements(By.cssSelector("td"));

            System.out.println("**************************" + headerRow.get(1).getText());
            String noHeader = headerRow.get(1).getText();
            String typeOf = headerRow.get(8).getText();

            pageArrows.get(j).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            List<WebElement> rowsDetails = proformaManager.getDetailTabel();

            for (int i = 0; i < rowsDetails.size(); i++) {
                System.out.println(i);
                ProformaManager.OrderData rowData = proformaManager.saveDataFromRow(i, rowsDetails);

                softAssert.assertTrue(verifyLinealMeters(rowData), "For no " + noHeader + " in table at index = " + i + " lineal meters have a bad value eg:" + rowData.fixed * rowData.count + "!=" + rowData.linearMeters);

                softAssert.assertTrue(verifyPackageVolume(rowData), "For no " + noHeader + " in table at index = " + i + " m3/packets have a bad value eg :" + rowData.width * rowData.thickness * rowData.fixed * rowData.count / 1000000 + "!=" + rowData.m3packets);
                softAssert.assertTrue(verifyPackageArea(rowData), "For no " + noHeader + " in table at index = " + i + " area " + rowData.linearMeters * rowData.width / 1000 + "!=" + rowData.m2packets);
                softAssert.assertTrue(verifyTotalM3(rowData), "For no " + noHeader + " in table at index = " + i + " total volume: " + rowData.m3packets * rowData.packets + "!= " + rowData.totalM3);
                if (!typeOf.equals("pieces"))
                    softAssert.assertTrue(verifyTotalPriceFromM3(rowData), "For no " + noHeader + " in table at index = " + i + " price calculate from volume price :" + rowData.width * rowData.thickness * rowData.fixed * rowData.count * rowData.packets * rowData.priceM3 / 1000000 + " != " + rowData.total);
                else
                    softAssert.assertTrue(verifyTotalPriceFromPiecesPrice(rowData), "For no " + noHeader + " in table at index = " + i + " price calculate from pieces price :" + rowData.fixed * rowData.priceM3 + rowData.count + " != " + rowData.total);

                softAssert.assertTrue(verifyTotalPriceFromPricePacket(rowData), "For no " + noHeader + " in table at index = " + i + " the total price calculate from package price: " + rowData.width * rowData.thickness * rowData.fixed * rowData.count * rowData.packets * rowData.priceM3 / 1000000 + "!=" + rowData.total);
            }

            ProformaManager.OrderFinalData orderFinalData = proformaManager.saveDataFromTableFooter();
            softAssert.assertTrue(verifyTotalCount(orderFinalData, rowsDetails), "For no " + noHeader + " Total number from footer is wrong " + orderFinalData.totalCount);
            softAssert.assertTrue(verifyTotalLinearMeters(orderFinalData, rowsDetails), "For no " + noHeader + " Total lineal meters is bad " + orderFinalData.totalLinearMeters);
            softAssert.assertTrue(verifyTotalPackets(orderFinalData, rowsDetails), "For no " + noHeader + " Total packets from footer is wrong" + orderFinalData.totalPackets);
            softAssert.assertTrue(verifyTotalM3Footer(orderFinalData, rowsDetails), "For no " + noHeader + " Total volume from footer is wrong " + orderFinalData.totalM3Final);
            softAssert.assertTrue(verifyTotalPrice(orderFinalData, rowsDetails), "For no " + noHeader + " Total prise from footer is wrong " + orderFinalData.totalMoney);

            pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));
            pageArrows.get(j).click();
            new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));

            headersList = getWebDriver().findElements(By.cssSelector(HEADERS_LIST_SELECTOR));
            headerRow = headersList.get(j).findElements(By.cssSelector("td"));
            softAssert.assertTrue(verifyHeaderValue(headerRow.get(5), orderFinalData.totalMoney), "For no " + noHeader + " total money 1 is wrong " + orderFinalData.totalMoney);
            softAssert.assertTrue(verifyHeaderValue(headerRow.get(6), orderFinalData.totalMoney), "For no " + noHeader + " total money 2 is wrong " + orderFinalData.totalMoney);
            softAssert.assertTrue(verifyPAcketsCountFromHeader(headerRow.get(9), orderFinalData.totalPackets), "For no " + noHeader + " total packets is wrong " + headerRow.get(9).getText() + " != " + orderFinalData.totalPackets);
            UtilsTools.scrollWitJavaScrip(0, 45, getWebDriver());
        }
        softAssert.assertAll();
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
        System.out.println(aux);
        System.out.println(rowData.m3packets);
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

    public boolean verifyTotalCount(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        int totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).count;

        return totalCount == orderFinalData.totalCount;
    }


    public boolean verifyTotalLinearMeters(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).linearMeters;

        return totalCount == orderFinalData.totalLinearMeters;
    }

    public boolean verifyTotalPackets(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).packets;

        return totalCount == orderFinalData.totalPackets;
    }

    public boolean verifyTotalM3Footer(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).totalM3;
        totalCount = Float.parseFloat(proformaManager.decimalFormatExtended.format(totalCount));

        return totalCount == orderFinalData.totalM3Final;
    }

    public boolean verifyTotalPrice(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).total;
        totalCount = Float.parseFloat(proformaManager.decimalFormatShort.format(totalCount));
        return totalCount == orderFinalData.totalMoney;
    }

    private boolean verifyHeaderValue(WebElement headerValue, float totalMoney) {
        Float total = Float.parseFloat(headerValue.getText().replaceAll("[^\\d.]", ""));
        total = Float.parseFloat(proformaManager.decimalFormatShort.format(total));
        totalMoney = Float.parseFloat(proformaManager.decimalFormatShort.format(totalMoney));

        return total == totalMoney;
    }

    private boolean verifyPAcketsCountFromHeader(WebElement headerCount, float countTotal) {
        Integer count = Integer.parseInt(headerCount.getText());
        return count == countTotal;
    }

}
