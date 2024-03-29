!Object.assign && Object.defineProperty(Object, "assign", {
    enumerable: false,
    configurable: true,
    writable: true,
    value: function (target, firstSource) {
        "use strict";
        if (target === undefined || target === null)
            throw new TypeError("Cannot convert first argument to object");
        var to = Object(target);
        for (var i = 1; i < arguments.length; i++) {
            var nextSource = arguments[i];
            if (nextSource === undefined || nextSource === null) continue;
            var keysArray = Object.keys(Object(nextSource));
            for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
                var nextKey = keysArray[nextIndex];
                var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
                if (desc !== undefined && desc.enumerable) to[nextKey] = nextSource[nextKey];
            }
        }
        return to;
    }
});
!Date.prototype.Format && (
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        };
        /(y+)/.test(fmt) && (fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)));
        for (var k in o) new RegExp("(" + k + ")").test(fmt) && (fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length))));
        return fmt;
    }
);
(function () {
    var requestConfig = {method: 'GET', baseURL: '/api/', timeout: 10000, headers: {}};
    var bw = {};
    bw.list = [];
    var Common = {
        windowWidth: Math.max(document.documentElement.clientWidth, window.innerWidth),
        windowHeight: Math.max(document.documentElement.clientHeight, window.innerHeight),
        toast: function (txt) {
            var i = 38;
            bw.list.length && bw.list.forEach(function (element, index) {
                element.setAttribute('style', 'bottom: ' + (bw.list.length - index) * i + 'px;');
            });
            var toast = document.createElement('div');
            toast.classList = 'bw-toast f-tac';
            toast.setAttribute('style', 'bottom: -36px;z-index:9999;');
            var msg = document.createTextNode(txt);
            toast.append(msg);
            bw.list.push(toast);
            document.body.append(toast);
            setTimeout(function () {
                toast.style['bottom'] = '50px';
                toast.style['opacity'] = '.5';
            }, 0);
            setTimeout(function () {
                toast.parentNode.removeChild(toast);
                bw.list.shift();
            }, 2000);
        },
        uploadFile: function (params) {
            var data = new FormData();
            for (var k in params.data) {
                data.append(k, params.data[k]);
            }
            params.config = params.config || {};
            params.config.method = 'POST';
            params.data = data;
            this.request(params);
        },
        request: function (params) {
            var config = Object.assign({}, requestConfig, params.config);
            params.data && (config.method == 'GET' ? config.params = params.data : config.data = params.data);
            params.url && (config.baseURL += params.url);
            axios(config).then(function (res) {
                res.data.code == 0 ? typeof params.success === 'function' && params.success(res) : typeof params.failure === 'function' && params.failure(res);
                typeof params.always === 'function' && params.always(res);
            }).catch(function (res) {
                typeof params.error === 'function' && params.error(res);
                typeof params.always === 'function' && params.always(res);
            })
        }
    };
    window['CommonUtils'] = Common;
})();