var app = angular.module('dongyimai', []);
/**
 * 放行HTML 过滤器
 */
app.filter("trustHtml", ['$sce', function ($sce) {
    return function (data) {
        return $sce.trustAsHtml(data);
    }
}]);
//{{entity.price}}