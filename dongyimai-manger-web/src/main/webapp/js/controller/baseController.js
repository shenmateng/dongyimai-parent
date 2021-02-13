//基本控制层
app.controller('baseController', function ($scope) {

    //重新加载列表 分页
    $scope.reloadList = function () {
        //切换页码
        //$scope.findPage( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);

    };
    //分页参数的配置
    $scope.paginationConf = {
        currentPage: 1,   //当前页数
        totalItems: 10,   //总记录数
        itemsPerPage: 10, //每一页显示多少条数据
        perPageOptions: [10, 20, 30, 40, 50],  //每一页的选项
        onChange: function () {
            $scope.reloadList(); //重新加载
        }
    };

    //定义一个数组来存放要删除的品牌的ID
    $scope.selectIds = [];
    //将要删除的品牌的id存入数组
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {//如果是被选中,则增加到数组
            $scope.selectIds.push(id);
        } else {
            //找到要删除ID的位置
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除
        }
    }

    //提取json字符串数据中某个属性，返回拼接字符串 逗号分隔
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);//将json字符串转换为json对象
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ","
            }
            value += json[i][key];
        }
        return value;
    }

});