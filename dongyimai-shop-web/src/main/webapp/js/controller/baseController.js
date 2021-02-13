app.controller("baseController", function ($scope) {
    $scope.reloadList = function () {
        //切换页码
        //$scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };
    //分页参数的配置
    $scope.paginationConf = {
        currentPage: 1, //当前页
        totalItems: 10, //每页记录数
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50], //每页显示的记录数的选项
        //onChange时间
        onChange: function () {
            $scope.reloadList();
        }
    };
    //定义一个数组来存放要删除的品牌的ID
    $scope.selectIds = [];
    //1.将要删除的品牌的ID存入数组
    $scope.updateSelection = function ($event, id) {
        //被选中的品牌被加入数组
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            //找到要删除Id的位置
            var index = $scope.selectIds.indexOf(id);
            //将ID从数组中删除
            $scope.selectIds.splice(index, 1);
        }

    };
    //将页面中JSON数据转换成字符串显示 jfreechar
    $scope.jsonToString = function (json, key) {
        var jsonValue = JSON.parse(json);
        var value = '';
        for (i = 0; i < jsonValue.length; i++) {
            if (i > 0) {
                value += ',';
            }
            value += jsonValue[i][key];
        }
        return value;
    }

    //从集合中按照key查询对象
    $scope.searchObjectByKey = function (list, key, keyValue) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key] == keyValue) {

                return list[i];
            }
        }
        return null;
    }
});