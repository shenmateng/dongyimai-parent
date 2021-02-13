app.controller('baseController', function ($scope) {
    //重新加载列表 分页
    $scope.reloadList = function () {
        //切换页码
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
    //被选中的ID 的数组
    $scope.selectIds = [];

    //选中/反选
    $scope.updateSelection = function ($event, id) {
        //判断复选框是否选中
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);   //参数一：元素的下标  参数二：移除的个数
        }
    }
    $scope.jsonToString = function (jsonString, key) {
        var value = '';
        if (jsonString != null) {
            var json = JSON.parse(jsonString);

            for (var i = 0; i < json.length; i++) {
                if (i > 0) {
                    value = value + ',';
                }
                value = value + json[i][key];
            }
        }
        return value;
    }
});