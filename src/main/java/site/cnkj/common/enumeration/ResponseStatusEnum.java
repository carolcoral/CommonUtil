package site.cnkj.common.enumeration;

/**
 * 返回报文状态码枚举值
 *
 * @author Carol, 2021-08-31
 **/
public enum ResponseStatusEnum {

    CONTINUE(100, "服务器已经接收到请求头, 并且客户端应继续发送请求主体."),
    SWITCHING_PROTOCOLS(101, "服务器已经理解了客户端的请求, 并将通过Upgrade消息头通知客户端采用不同的协议来完成这个请求."),
    PROCESSION(102, "WebDAV请求可能包含许多涉及文件操作的子请求, 需要很长时间才能完成请求."),
    EARLY_HINTS(103, "用来在最终的HTTP消息之前返回一些响应头."),
    SUCCESS(200, "请求已成功, 请求所希望的响应头或数据体将随此响应返回."),
    CREATED(201, "请求已经被实现, 而且有一个新的资源已经依据请求的需要而创建, 且其URI已经随Location头信息返回."),
    ACCEPTED(202, "服务器已接受请求, 但尚未处理。最终该请求可能会也可能不会被执行, 并且可能在处理发生时被禁止."),
    NON_AUTHORITATIVE_INFORMATION(203, "服务器是一个转换代理服务器, 以200 OK状态码为起源, 但回应了原始响应的修改版本."),
    NO_CONTENT(204, "服务器成功处理了请求, 没有返回任何内容."),
    RESET_CONTENT(205, "服务器成功处理了请求, 但没有返回任何内容."),
    PARTIAL_CONTENT(206, "服务器已经成功处理了部分GET请求."),
    MULTI_STATUS(207, "代表之后的消息体将是一个XML消息, 并且可能依照之前子请求数量的不同, 包含一系列独立的响应代码."),
    ALREADY_REPORTED(208, "DAV绑定的成员已经在（多状态）响应之前的部分被列举, 且未被再次包含."),
    IM_USED(226, "服务器已经接收到请求头, 并且客户端应继续发送请求主体."),
    MULTIPLE_CHOICES(300, "被请求的资源有一系列可供选择的回馈信息."),
    MOVED_PERMANENTLY(301, "被请求的资源已永久移动到新位置."),
    FOUND(302, "要求客户端执行临时重定向."),
    SEE_OTHER(303, "对应当前请求的响应可以在另一个URI上被找到."),
    NOT_MODIFIED(304, "表示资源在由请求头中的If-Modified-Since或If-None-Match参数指定的这一版本之后, 未曾被修改."),
    USE_PROXY(305, "被请求的资源必须通过指定的代理才能被访问."),
    SWITCH_PROXY(306, "后续请求应使用指定的代理."),
    TEMPORARY_REDIRECT(307, "请求应该与另一个URI重复, 但后续的请求应仍使用原始的URI."),
    PERMANENT_REDIRECT(308, "请求和所有将来的请求应该使用另一个URI重复."),
    BAD_REQUEST(400, "由于明显的客户端错误, 服务器不能或不会处理该请求."),
    UNAUTHORIZED(401, "当前请求需要用户验证."),
    FORBIDDEN(403, "服务器已经理解请求, 但是拒绝执行它."),
    NOT_FOUND(404, "请求所希望得到的资源未被在服务器上发现."),
    METHOD_NOT_ALLOWED(405, "请求行中指定的请求方法不能被用于请求相应的资源."),
    NOT_ACCEPTABLE(406, "请求的资源的内容特性无法满足请求头中的条件."),
    PROXY_AUTHENTICATION_REQUIRED(407, "客户端必须在代理服务器上进行身份验证."),
    REQUEST_TIMEOUT(408, "请求超时."),
    CONFLICT(409, "请求存在冲突无法处理该请求."),
    GONE(410, "所请求的资源不再可用."),
    LENGTH_REQUIRED(411, "服务器拒绝在没有定义Content-Length头的情况下接受请求."),
    PRECONDITION_FAILED(412, "服务器在验证在请求的头字段中给出先决条件时, 没能满足其中的一个或多个."),
    REQUEST_ENTITY_TOO_LARGE(413, "服务器拒绝处理当前请求, 该请求提交的实体数据大小超过了服务器愿意或者能够处理的范围."),
    REQUEST_URL_TOO_LONG(414, "请求的URI长度超过了服务器能够解释的长度."),
    UNSUPPORTED_MEDIA_TYPE(415, "请求中提交的互联网媒体类型并不是服务器中所支持的格式."),
    REQUESTED_RANG_NOT_SATISFIABLE(416, "客户端已经要求文件的一部分（Byte serving）, 但服务器不能提供该部分."),
    EXPECTATION_FAILED(417, "在请求头Expect中指定的预期内容无法被服务器满足."),
    MISDIRECTED_REQUEST(421, "无法产生响应的服务器."),
    UNPROCESSABLE_ENTITY(422, "请求格式正确, 但是由于含有语义错误, 无法响应."),
    LOCKED(423, "当前资源被锁定."),
    FAILED_DEPENDENCE(424, "由于之前的某个请求发生的错误, 导致当前请求失败."),
    TOO_EARLY(425, "服务器拒绝处理在Early Data中的请求."),
    UPGRADE_REQUIRED(426, "客户端应切换到Upgrade头字段中给出的不同协议."),
    PRECONDITION_REQUIRED(428, "原服务器要求该请求满足一定条件."),
    TOO_MANY_REQUESTS(429, "给定的时间内发送了太多的请求."),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "服务器不愿处理请求, 因为一个或多个头字段过大."),
    LOGIN_TIMEOUT(440, "客户端session超时失效, 需要重新登录."),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "该访问因法律的要求而被拒绝."),
    INTERNAL_SERVER_ERROR(500, "服务器通用错误消息."),
    NOT_IMPLEMENTED(501, "服务器不支持当前请求所需要的某个功能."),
    BAD_GATEWAY(502, "作为网关或者代理工作的服务器尝试执行请求时, 从上游服务器接收到无效的响应."),
    SERVICE_UNAVAILABLE(503, "由于临时的服务器维护或者过载, 服务器当前无法处理请求."),
    GATEWAY_TIMEOUT(504, "作为网关或者代理工作的服务器尝试执行请求时, 未能及时从上游服务器收到响应."),
    HTTP_VERSION_NOT_SUPPORTED(505, "服务器不支持, 或者拒绝支持在请求中使用的HTTP版本."),
    VARIANT_ALSO_NEGOTIATES(506, "服务器存在内部配置错误."),
    INSUFFICIENT_STORAGE(507, "服务器无法存储完成请求所必须的内容."),
    LOOP_ERECTED(508, "服务器在处理请求时陷入死循环."),
    NOT_EXTENDED(510, "获取资源所需要的策略并没有被满足."),
    NETWORK_AUTHENTICATION_REQUIRED(511, "客户端需要进行身份验证才能获得网络访问权限.");

    private final int code;

    private final String desc;

    ResponseStatusEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
