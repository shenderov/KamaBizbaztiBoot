package com.kamabizbazti.restcontroller;

import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.http.HttpConnectorGeneral;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations="classpath:test.properties")
public class GeneralRestControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @LocalServerPort
    private int port;

    private HttpConnectorGeneral connector;

    @BeforeClass
    public void setup() {
        connector = new HttpConnectorGeneral(port);
    }

    @Test
    public void testGetGeneralChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllGeneralSelections();
        List<ChartSelection> selectionsHttp = connector.getGeneralChartSelectionsList();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetUserChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllUserSelections();
        List<ChartSelection> selectionsHttp = connector.getUserChartSelectionsList();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetDefaultDataTable() throws Exception {
        ChartWrapper wrapper = connector.getDefaultDataTable();
        ChartSelection selection = chartSelectionRepository.findOne(GeneralRequestHandler.DEFAULT_CHART_SELECTION);
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
        Assert.assertEquals(wrapper.getTitle(), selection.getTitle());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
    }

    @Test
    public void testGetGeneralDataTable() throws Exception {
        List<ChartSelection> selections = connector.getGeneralChartSelectionsList();
        for (ChartSelection c : selections) {
            ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
            requestWrapper.setChartSelection(c);
            ChartWrapper wrapper = connector.getGeneralDataTable(requestWrapper);
            Assert.assertNotNull(wrapper);
            Assert.assertNotNull(wrapper.getDataTable());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
            Assert.assertEquals(c.getTitle(), wrapper.getTitle());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
        }
    }
}
