//商品类目控制层 
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体 
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存 
    $scope.save = function () {
        var serviceObject;//服务层对象  				
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改  
        } else {
            $scope.entity.parentId = $scope.parentId;//赋予上级ID
            serviceObject = itemCatService.add($scope.entity);//增加 
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询 
                    $scope.findItemByParentId($scope.parentId);//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除 
    $scope.dele = function () {
        //获取选中的复选框			
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.findItemByParentId($scope.parentId);//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {"ParentId": 0};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        itemCatService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
    $scope.parentId = 0;//上级ID

    // 社保软件  双向绑定 react/vue
    $scope.findItemByParentId = function (parentId) {
        $scope.parentId = parentId;//记住上级ID
        itemCatService.findItemByParentId(parentId).success(function (response) {
            $scope.list = response;
        });
    };


    //<c:if></c:if>  <ng:if> grade !=3
    $scope.grade = 1;//将grade设置为1 2 3
    $scope.setGrade = function (value) {
        $scope.grade = value;
    };
    //1.当在顶级分类时，二级/三级不能出来
    //2当在二级分类时 三级不能出来
    $scope.selectList = function (p_entity) {
        //=== 绝对等 地址值值相等  == 值相等
        // == 绝对等       equals() 值相等
        //当顶级商品列表 显示 1级2级都不显示
        if ($scope.grade == 1) {
            $scope.entity_1 = null;
            $scope.entity_2 = null;
        }
        //2.当2级列表出现 顶级显示
        if ($scope.grade == 2) {
            $scope.entity_1 = p_entity;
            $scope.entity_2 = null;
        }
        //当第3级出现 1级2级都显示
        if ($scope.grade == 3) {
            $scope.entity_2 = p_entity;
        }
        //当点击分类列表时，下面必须刷新页面
        $scope.findItemByParentId(p_entity.id);
    }


    $scope.typeTemplateList = {data: []};//模板列表
    //读取模板列表
    $scope.findtypeTemplateList = function () {
        typeTemplateService.selectOptionList().success(
            function (response) {
                $scope.typeTemplateList = {data: response};
            }
        );
    }


});	