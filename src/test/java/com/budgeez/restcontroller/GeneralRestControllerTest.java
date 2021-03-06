package com.budgeez.restcontroller;

import com.google.gson.JsonObject;
import com.budgeez.BudgeezBootApplicationTests;
import com.budgeez.common.entities.HttpResponseJson;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.external.ChartWrapper;
import com.budgeez.model.entities.external.EVersion;
import com.budgeez.model.enumerations.ChartSelectionIdEnum;
import com.budgeez.model.enumerations.ChartType;
import com.budgeez.model.exceptions.codes.DataIntegrityErrorCode;
import com.budgeez.model.exceptions.codes.EntitiesErrorCode;
import com.budgeez.model.handlers.GeneralRequestHandler;
import com.budgeez.model.handlers.UserRequestHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class GeneralRestControllerTest extends BudgeezBootApplicationTests {

    private static final ChartSelectionIdEnum DEFAULT_CHART_SELECTION = ChartSelectionIdEnum.CURRENT_MONTH_AVG;

    @Test
    public void testGetGeneralChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllGeneralSelections();
        List<ChartSelection> selectionsHttp = (List<ChartSelection>) generalRestControllerConnectorHelper.getGeneralChartSelectionsListPositive().getObject();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetUserChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllUserSelections();
        List<ChartSelection> selectionsHttp = (List<ChartSelection>) generalRestControllerConnectorHelper.getUserChartSelectionsListPositive().getObject();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetDefaultDataTable() {
        ChartWrapper wrapper = (ChartWrapper) generalRestControllerConnectorHelper.getDefaultDataTablePositive().getObject();
        ChartSelection selection = chartSelectionRepository.findOne(DEFAULT_CHART_SELECTION);
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
        Assert.assertEquals(wrapper.getTitle(), selection.getTitle());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
    }

    @Test
    public void testGetGeneralDataTable() throws Exception {
        List<ChartSelection> selections = (List<ChartSelection>) generalRestControllerConnectorHelper.getGeneralChartSelectionsListPositive().getObject();
        for (ChartSelection c : selections) {
            ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
            requestWrapper.setChartSelection(c);
            ChartWrapper wrapper = (ChartWrapper) generalRestControllerConnectorHelper.getGeneralDataTablePositive(requestWrapper).getObject();
            Assert.assertNotNull(wrapper);
            Assert.assertNotNull(wrapper.getDataTable());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
            Assert.assertEquals(c.getTitle(), wrapper.getTitle());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
        }
    }

    @Test
    public void testGetGeneralDataTableWrongSelection() throws Exception {
        String wrongSelectionId = "WRONG";
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG));
        JsonObject jsonObject = testTools.objectToJsonObject(requestWrapper);
        jsonObject.get("chartSelection").getAsJsonObject().remove("selectionId");
        jsonObject.get("chartSelection").getAsJsonObject().addProperty("selectionId", wrongSelectionId);
        HttpResponseJson response = generalRestControllerConnectorHelper.getGeneralDataTableNegative(jsonObject.toString()).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testGetGeneralDataTableWithSelectionIdOnlyIsValid() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) generalRestControllerConnectorHelper.getGeneralDataTablePositive(requestWrapper).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetGeneralDataTableWithValidSelectionAndInvalidOtherFields() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.PREV_MONTH_AVG);
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setTitle("title");
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) generalRestControllerConnectorHelper.getGeneralDataTablePositive(requestWrapper).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetGeneralDataTableNullSelection() throws Exception {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(null);
        HttpResponseJson response = generalRestControllerConnectorHelper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testGetGeneralDataTableWithoutSelection() throws Exception {
        HttpResponseJson response = generalRestControllerConnectorHelper.getGeneralDataTableNegative("{}").convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testGetGeneralDataTableWithUserSelection() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(UserRequestHandler.DEFAULT_CHART_SELECTION);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        HttpResponseJson response = generalRestControllerConnectorHelper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Unknown chart selection ID");
    }

    @Test
    public void testGetVersion() throws Exception {
        EVersion version = (EVersion) generalRestControllerConnectorHelper.getVersion().getObject();
        Assert.assertNotNull(version);
        Assert.assertEquals(version.getName(), "Budgeez");
    }

    @Test
    public void testGetLanguages() throws Exception {
        List<Language> languages = (List<Language>) generalRestControllerConnectorHelper.getLanguages().getObject();
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.size() > 1);
    }

    @Test
    public void testGetCurrencies() throws Exception {
        List<Currency> currencies = (List<Currency>) generalRestControllerConnectorHelper.getCurrencies().getObject();
        Assert.assertNotNull(currencies);
        Assert.assertTrue(currencies.size() > 1);
    }
}