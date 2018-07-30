package de.test.automatedTests.actionsMenuTests;

import de.test.automatedTests.config.AbstractAcceptanceTest;
import de.test.automatedTests.managers.ApplicationManager;
import de.test.automatedTests.managers.ProformaManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static de.test.automatedTests.managers.ProformaManager.ARROW_SELECTOR;
import static de.test.automatedTests.managers.ProformaManager.HEADERS_LIST_SELECTOR;
import static de.test.automatedTests.utils.LoginUtils.loginOnPage;

public class ProformaAcceptanceTests extends AbstractAcceptanceTest {


    @Test
    public void blabla() {
        //login on the page
        loginOnPage("admin", "654321", getWebDriver());
        homePageMananger.goToPraformaPage("Actions", "Proforma Invoices");

        List<WebElement> pageArrows = getWebDriver().findElements(By.cssSelector(ARROW_SELECTOR));
        pageArrows.get(0).click();

        new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_20_SECONDS).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".t-font-icon.rgIcon.rgCollapseIcon")));
        List<WebElement> rowsDetails = proformaManager.getDetailTabel();

        for (int i = 0; i < rowsDetails.size(); i++) {
            if (i != 2) {
                ProformaManager.OrderData rowData = proformaManager.saveDataFromRow(i, rowsDetails);

                Assert.assertTrue(verifyLinealMeters(rowData), "the lineal meters have a bad value for " + rowData.fixed + "*" + rowData.count + "!=" + rowData.linearMeters);
                Assert.assertTrue(verifyPackageVolume(rowData), "the m3/packets have a bad value for " + rowData.width + "*" + rowData.thickness + "*" + rowData.fixed + "*" + rowData.count + "=" + rowData.m3packets);
                Assert.assertTrue(verifyPackageArea(rowData), "the area " + rowData.linearMeters * rowData.width / 1000 + " need to be " + rowData.m2packets);
                Assert.assertTrue(verifyTotalM3(rowData), "the total volume " + rowData.m3packets * rowData.packets + " need to be " + rowData.totalM3);
                Assert.assertTrue(verifyTotalPriceFromM3(rowData), "the total price calculate from volume price " + rowData.totalM3 * rowData.priceM3 + " but it is " + rowData.total);
                Assert.assertTrue(verifyTotalPriceFromPricePacket(rowData), "the total price calculate from package price " + rowData.packets * rowData.pricePacket + " but it is " + rowData.total);
            }
        }

        ProformaManager.OrderFinalData orderFinalData = proformaManager.saveDataFromTableFooter();
        Assert.assertTrue(verifyTotalCount(orderFinalData, rowsDetails),"the total number from footer is wrong "+orderFinalData.totalCount);
        Assert.assertTrue(verifyTotalLinearMeters(orderFinalData, rowsDetails),"the total lineal meters is bad "+orderFinalData.totalLinearMeters);
        Assert.assertTrue(verifyTotalPackets(orderFinalData, rowsDetails),"the total packets from footer is wrong"+orderFinalData.totalPackets);
        Assert.assertTrue(verifyTotalM3Footer(orderFinalData, rowsDetails),"the total vloume from footer is wrong "+orderFinalData.totalM3Final);
        Assert.assertTrue(verifyTotalPrice(orderFinalData, rowsDetails),"the total prise from footer is wrong "+orderFinalData.totalMoney);

        pageArrows.get(0).click();
        List<WebElement> headerList=getWebDriver().findElements(By.cssSelector(HEADERS_LIST_SELECTOR));
        List<WebElement> headerRow=headerList.get(0).findElements(By.cssSelector("td"));

    }

    //verify linear meters that is calculate like a multiplication between fixed and count
    public boolean verifyLinealMeters(ProformaManager.OrderData rowData) {

        return rowData.fixed * rowData.count == rowData.linearMeters;
    }

    //verify the volume with formula width*thickness*count*fixed and divided with 1000000 to obtain cube feet
    public boolean verifyPackageVolume(ProformaManager.OrderData rowData) {

        float aux = rowData.width * rowData.thickness * rowData.count / 1000000 * rowData.fixed;
        aux = BigDecimal.valueOf(aux).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
        return aux == rowData.m3packets;
    }

    //verify the total area from linearMeters*width and divide with 10000 for  square feet
    public boolean verifyPackageArea(ProformaManager.OrderData rowData) {

        float aux = rowData.linearMeters * rowData.width / 1000;
        aux = BigDecimal.valueOf(aux).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
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
        DecimalFormat df = new DecimalFormat("#.##");
        aux = Float.parseFloat(df.format(aux));
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

        DecimalFormat df = new DecimalFormat("#.###");
        totalCount = Float.parseFloat(df.format(totalCount));

        return totalCount == orderFinalData.totalM3Final;
    }

    public boolean verifyTotalPrice(ProformaManager.OrderFinalData orderFinalData, List<WebElement> rowsDetails) {

        float totalCount = 0;

        for (int i = 0; i < rowsDetails.size(); i++)
            totalCount += proformaManager.saveDataFromRow(i, rowsDetails).total;

        return totalCount == orderFinalData.totalMoney;
    }
}
