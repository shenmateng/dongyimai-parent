app.service('uploadService', function ($http) {
    this.upload = function () {
        var formData = new FormData();//拿到表单数据
        formData.append("file", file.files[0])//取第一个数据
        return $http({
            method: 'POST',
            url: '../upload.do',
            data: formData,
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity

        });

    };
});