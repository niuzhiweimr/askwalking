# 网关配置说明
 
### 1.0版本

配置项|key|默认值|可选|说明
----|----|-----|----|---
应用名称|applicationName||必选|调用服务注册名 
api类型|apiType||必选|参数 API：(外部接口) ADMIN：(管理后台接口) SAAS：(开放平台接口) 
请求URI|requestUri||必选|自定义请求URI必须不能重复,建议url加入版本号方便后续接口升级 例：/uid/v1/generator 
请求方式|requestMethod|GET|可选|参数 GET、POST 
接口地址|apiInterface||必选|接口全类名,例：com.awservice.openapi.service.TestService 
接口版本|apiVersion||可选|接口上定义的版本号必须与此配置版本号对应 
请求对象|apiRequestClass||必选|接口请求参数对象全类名,例：com.awservice.account.AddAccountRequest 
方法名称|apiMethod||必选|接口方法,例：test  
接口分组|apiGroup||可选|接口上定义的分组必须与此配置分组对应 
接口名称|apiName||可选| 参考 根据自己提供接口功能定义 
异步支持|apiAsync|0|可选|参数 0：同步 1：异步 
重载支持|apiReload|1|可选|参数 0：不支持 1：支持  注：对参数qps 、avgTt、maxThread生效        
mock数据|mockResponse||可选|此数据必须为json格式,否则网关报错：json序列化失败             
系统保护|systemGuard|1|可选|开启系统保护后限流规则生效,参数 0：开启 1：不开启 
限流类型|flowControlRuleType|2|可选|参数 1：系统限流目前不支持 2：api限流   
每秒请求数|qps|-1|可选|类型为浮点类型    
平均响应时间|avgTt|-1|可选|单位为毫秒ms      
最大并发数|maxThread|-1|可选|入口流量的最大并发数      
签名校验|signCheck|0|必选|参数 0：不校验，1：校验     
接口状态|status|2|可选|参数 0：mock_response报文，1：待发布，2：已发布                        |
