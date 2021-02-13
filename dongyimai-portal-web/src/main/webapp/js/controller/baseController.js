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
    $scope.selectedIds = [];
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectedIds.push(id);
        } else {
            let index = $scope.selectedIds.indexOf(id);
            $scope.selectedIds.splice(index, 1);
        }
        ;
    };
    $scope.jsonToString = function (jsonString, key) {
        let value = '';
        if (jsonString != null) {
            let json = JSON.parse(jsonString);

            for (let i = 0; i < json.length; i++) {
                if (i > 0) {
                    value = value + ',';
                }
                value = value + json[i][key];
            }
        }
        return value;
    }
});