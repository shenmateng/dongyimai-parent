//首页控制器
app.controller('indexController', function ($scope, loginService) {

    //获取登录名
    $scope.showName = function () {
        loginService.showName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    }
});