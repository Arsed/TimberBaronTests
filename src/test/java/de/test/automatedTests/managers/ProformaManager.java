package de.test.automatedTests.managers;

import de.test.automatedTests.config.AbstractAcceptanceTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DecimalFormat;
import java.util.List;

public class ProformaManager extends AbstractAcceptanceTest {

    private WebDriver driver;

    public ProformaManager(WebDriver driver) {
        this.driver = driver;
    }

    public static DecimalFormat decimalFormatExtended = new DecimalFormat("#.###");

    public static DecimalFormat decimalFormatShort = new DecimalFormat("#.##");

    public static String ARROW_SELECTOR = ".rgRow .rgExpandCol";
    public static String DETAIL_TABLE_FOOTER = ".rgDetailTable.rgClipCells .rgFooter td";
    public static String HEADERS_LIST_SELECTOR = ".rgRow";
    public static String PAGE_TITLE = ".page-title";

    public class OrderData {
        public float fixed;
        public int count;
        public float linearMeters;
        public int packets;
        public float m3packets;
        public float m2packets;
        public float totalM3;
        public float priceM3;
        public float pricePacket;
        public float total;
        public float width;
        public float thickness;
        public String specie;
        public String grade;
        public String drynes;
        public String treatment;
        public String finish;

        OrderData(float fixed,
                  int count,
                  float linearMeters,
                  int packets,
                  float m3packets,
                  float m2pachets,
                  float totalM3,
                  float priceM3,
                  float pricePacket,
                  float total,
                  float width,
                  float thickness,
                  String specie,
                  String grade,
                  String drynes,
                  String treatment,
                  String finish) {
            this.fixed = fixed;
            this.linearMeters = linearMeters;
            this.count = count;
            this.packets = packets;
            this.m3packets = m3packets;
            this.m2packets = m2pachets;
            this.totalM3 = totalM3;
            this.priceM3 = priceM3;
            this.pricePacket = pricePacket;
            this.total = total;
            this.width = width;
            this.thickness = thickness;
            this.specie = specie;
            this.grade = grade;
            this.drynes = drynes;
            this.treatment = treatment;
            this.finish = finish;
        }
    }

    /**
     * create an inner class
     * it will be used to save data collected from a row of table
     */

    public List<WebElement> getDetailTabel() {


        //second table contain the rows that we need
        List<WebElement> tableList = getWebDriver().findElements(By.cssSelector("table"));

        //all the row with good date for us have class .rgRow
        List<WebElement> rows = tableList.get(1).findElements(By.cssSelector(".rgRow"));

        return rows;
    }

    public OrderData saveDataFromRow(int index, List<WebElement> rows) {
        float fixed;
        int count;
        float linearMeters;
        int packets;
        float m3packets;
        float m2packets;
        float totalM3;
        float priceM3;
        float pricePacket;
        float total;
        float width;
        float thickness;
        String specie;
        String grade;
        String drynes;
        String treatment;
        String finish;
        //save in list row data all the information from row "index"
        List<WebElement> rowData = rows.get(index).findElements(By.tagName("td"));
        specie = rowData.get(0).getText();
        grade = rowData.get(1).getText();
        drynes = rowData.get(2).getText();
        treatment = rowData.get(3).getText();
        finish = rowData.get(4).getText();
        width = Float.parseFloat(rowData.get(5).getText());
        thickness = Float.parseFloat(rowData.get(7).getText());
        fixed = Float.parseFloat(rowData.get(10).getText());
        count = Integer.parseInt(rowData.get(11).getText());
        linearMeters = Float.parseFloat(rowData.get(12).getText().replaceAll("[^\\d.]", ""));
        packets = Integer.parseInt(rowData.get(13).getText());
        m3packets = Float.parseFloat(rowData.get(14).getText());
        m2packets = Float.parseFloat(rowData.get(15).getText().replaceAll("[^\\d.]", ""));
        totalM3 = Float.parseFloat(rowData.get(16).getText());
        //remove the $ from price strings
        priceM3 = Float.parseFloat(rowData.get(17).getText().replaceAll("[^\\d.]", ""));
        pricePacket = Float.parseFloat(rowData.get(18).getText().replaceAll("[^\\d.]", ""));
        total = Float.parseFloat(rowData.get(19).getText().replaceAll("[^\\d.]", ""));

        //return an object with all collected dates
        OrderData orderData;
        orderData = new OrderData(fixed,
                count,
                linearMeters,
                packets,
                m3packets,
                m2packets,
                totalM3,
                priceM3,
                pricePacket,
                total,
                width,
                thickness,
                specie,
                grade,
                drynes,
                treatment,
                finish);
        return orderData;
    }

    public class OrderFinalData

    {
        public int totalCount;
        public float totalLinearMeters;
        public float totalPackets;
        public float totalM3Final;
        public float totalMoney;

        OrderFinalData(int totalCount, float totalLinearMeters, float totalM3Final, float totalPackets,
                       float totalMoney) {
            this.totalCount = totalCount;
            this.totalLinearMeters = totalLinearMeters;
            this.totalM3Final = totalM3Final;
            this.totalPackets = totalPackets;
            this.totalMoney = totalMoney;
        }

    }

    public OrderFinalData saveDataFromTableFooter() {
        List<WebElement> totalDetail = getWebDriver().findElements(By.cssSelector(DETAIL_TABLE_FOOTER));
        int count;
        float liniarMeters;
        float packets;
        float totalM3;
        float totalMoney;
        count = Integer.parseInt(totalDetail.get(11).getText());
        liniarMeters = Float.parseFloat(totalDetail.get(12).getText().replaceAll("[^\\d.]", ""));
        packets = Float.parseFloat(totalDetail.get(13).getText().replaceAll("[^\\d.]", ""));
        totalM3 = Float.parseFloat(totalDetail.get(16).getText().replaceAll("[^\\d.]", ""));
        totalMoney = Float.parseFloat(totalDetail.get(19).getText().replaceAll("[^\\d.]", ""));
        return new OrderFinalData(count, liniarMeters, totalM3, packets, totalMoney);
    }

    public void selectNumberOfElementsOnPage() {
        List<WebElement> pageButtons = driver.findElements(By.cssSelector(".rgWrap.rgNumPart a"));
        WebElement selectNumberOnPage = getWebDriver().findElement(By.cssSelector(".rcbInner.rcbReadOnly"));
        selectNumberOnPage.click();
        new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".rcbList li:nth-child(5)")));
        WebElement optionsNumberElementsOnPAge = getWebDriver().findElement(By.cssSelector(".rcbList li:nth-child(5)"));
        optionsNumberElementsOnPAge.click();
        new WebDriverWait(getWebDriver(), ApplicationManager.WAIT_TIME_OUT_IN_35_SECONDS).until(ExpectedConditions.invisibilityOf(pageButtons.get(1)));

    }

    public class ColumnsOfTabel

    {
        public String invoice;
        public String dateCreated;
        public String customer;
        public String currency;
        public String valueExGST;
        public String valueInGST;
        public String isGST;
        public String unit;
        public String vessel;
        public String voyageNo;
        public String dischargeDate;
        public String note;
        public WebElement printButton;


        ColumnsOfTabel(String invoice,
                       String dateCreated,
                       String customer,
                       String currency,
                       String valueExGST,
                       String valueInGST,
                       String isGST,
                       String unit,
                       String vessel,
                       String voyageNo,
                       String dischargeDate,
                       String note,
                       WebElement printButton) {
            this.invoice = invoice;
            this.dateCreated = dateCreated;
            this.customer = customer;
            this.currency = currency;
            this.valueExGST = valueExGST;
            this.valueInGST = valueInGST;
            this.isGST = isGST;
            this.unit = unit;
            this.vessel = vessel;
            this.voyageNo = voyageNo;
            this.dischargeDate = dischargeDate;
            this.note = note;
            this.printButton=printButton;
        }

    }

    public ColumnsOfTabel saveDataFromMainTabelRow(int index, List<WebElement> headersList) {
        String invoice;
        String dateCreated;
        String customer;
        String currency;
        String valueExGST;
        String valueInGST;
        String isGST;
        String unit;
        String vessel;
        String voyageNo;
        String dischargeDate;
        String note;
        WebElement printButton;
       List<WebElement> rowData = headersList.get(index).findElements(By.cssSelector("td"));
       invoice=rowData.get(1).getText();
       dateCreated=rowData.get(2).getText();
       customer=rowData.get(3).getText();
       currency=rowData.get(4).getText();
       valueExGST=rowData.get(5).getText();
       valueInGST=rowData.get(6).getText();
       isGST=rowData.get(7).getText();
       unit=rowData.get(8).getText();
       vessel=rowData.get(10).getText();
       voyageNo=rowData.get(11).getText();
        dischargeDate=rowData.get(12).getText();
       note=rowData.get(12).getText();
       printButton=rowData.get(15);

       return new ColumnsOfTabel(invoice,dateCreated,customer,currency,valueExGST,valueInGST,isGST,unit,vessel
       ,voyageNo,dischargeDate,note,printButton);
    }

}

