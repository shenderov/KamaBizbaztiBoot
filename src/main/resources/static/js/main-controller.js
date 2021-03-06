'use strict';

app.controller('MainController', function ($scope, $rootScope, $http, $templateCache, Connector) {
    $scope.chartSelectionsList = null;
    $scope.activeChartSelection = {};
    $scope.chartDatePicker = {};
    $scope.chartDatePicker.getChartForm = {};
    $scope.getChartSubmitDisable = false;
    $scope.chartWrapper = null;
    chartAuth = true;
    $rootScope.showCarousel = true;
    $scope.selectionDropdownDivider = false;

    $scope.getGeneralSelectionsList = function () {
        Connector.getGeneralSelectionsList()
            .then(
                function (chartSelectionsList) {
                    $scope.chartSelectionsList = chartSelectionsList;
                    $scope.activeChartSelection = $scope.chartSelectionsList[0];
                    angular.forEach($scope.chartSelectionsList, function(selection) {
                        if(selection.datePicker === true)
                            $scope.selectionDropdownDivider = true;
                    });
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                }
            );
    };
    $scope.getGeneralSelectionsList();

    $scope.getDefaultDataTable = function () {
        return Connector.getGeneralDefaultDataTable()
            .then(
                function (defaultChartWrapper) {
                    return defaultChartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                    return null;
                }
            )
    };

    $scope.getDataTable = function (chartRequestWrapper) {
        $scope.getChartSubmitDisable = true;
        return Connector.getGeneralDataTable(chartRequestWrapper)
            .then(
                function (chartWrapper) {
                    $scope.getChartSubmitDisable = false;
                    return chartWrapper;
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    $rootScope.addMainWindowAlert('danger', errResponse.message);
                    $scope.getChartSubmitDisable = false;
                    return null;
                }
            )
    };
});