(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
      // AMD. Register as an anonymous module unless amdModuleId is set
      define('simditor-submit', ["jquery","simditor"], function (a0,b1) {
        return (root['SimditorSubmit'] = factory(a0,b1));
      });
    } else if (typeof exports === 'object') {
      // Node. Does not work with strict CommonJS, but
      // only CommonJS-like environments that support module.exports,
      // like Node.
      module.exports = factory(require("jquery"),require("simditor"));
    } else {
      root['SimditorSubmit'] = factory(jQuery,Simditor);
    }
  }
  (this, function ($, Simditor) {
    var SimditorSubmit,
      extend = function(child, parent) {
        for (var key in parent) {
          if (hasProp.call(parent, key)) child[key] = parent[key];
        }
        function ctor() {
          this.constructor = child;
        }
        ctor.prototype = parent.prototype;
        child.prototype = new ctor();
        child.__super__ = parent.prototype; return child;
      },

      hasProp = {}.hasOwnProperty;

      SimditorSubmit = (function(superClass) {

      extend(SimditorSubmit, superClass);

      function SimditorSubmit() {
        return SimditorSubmit.__super__.constructor.apply(this, arguments);
      }

      SimditorSubmit.prototype.name = 'submit';

      SimditorSubmit.prototype.icon = 'submit';

      SimditorSubmit.prototype.disableTag = 'pre, table';

      SimditorSubmit.prototype.command = function() {
        alert("点击发布文章按钮！");
      };
      return SimditorSubmit;
    })(Simditor.Button);

    Simditor.Toolbar.addButton(SimditorSubmit);

    return SimditorSubmit;
  })
);
