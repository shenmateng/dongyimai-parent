<!--定义控制器-->
//定义控制层，需要注入service代码
app.controller('brandController', function ($scope, $controller, brandService) {
    $controller('baseController', {$scope: $scope});//继承
    //品牌查询方法
    $scope.findAll = function () {
        brandService.findAll.success(function (response) {
            $scope.list = response;  //定义数组,注意元素不能重复
        });

    };

    //分页
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;  //更新总记录数
            });
    };
    //保存数据
    $scope.save = function () {

        if ($scope.entity.id != null) { //如果有ID
            brandService.update($scope.entity).success(
                function (response) {
                    if (response.success) {
                        //重新查询
                        $scope.reloadList();//重新加载
                    } else {
                        alert(response.message);
                    }
                });
        } else {
            brandService.add($scope.entity).success(
                function (response) {
                    if (response.success) {
                        //重新查询
                        $scope.reloadList();//重新加载
                    } else {
                        alert(response.message);
                    }
                });
        }
    }
    //修改回显
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;

            });
    }

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

    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        brandService.dele($scope.selectIds).success(
            function (resonse) {
                if (resonse.success) {
                    $scope.reloadList();//刷新列表
                }
            });
    }
    $scope.searchEntity = {};//定义搜索对象
    //条件查询
    $scope.search = function (page, rows) {
        brandService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;//总记录数
                $scope.list = response.rows;//给列表变量赋值
            });
    }

});