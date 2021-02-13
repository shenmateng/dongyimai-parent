app.controller('indexController', function ($scope,loginService) {
    $scope.showLoginName = function () {
        loginService.getLoginName().success(function (response) {
            $scope.username= response.name;
        });
    }
});