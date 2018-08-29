FROM registry.cn-beijing.aliyuncs.com/duobeiyun-web/centos:latest

LABEL description="zhuanwangtong zwt-repertory image"

WORKDIR /zwt

COPY zwt-repertory.jar /zwt

EXPOSE 8888

CMD java -Xmx${JVM_MAX_MEMORY} -Xss1M -server -Djava.security.egd=file:/dev/./urandom \
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/duobei/zwt/logs \
    -Dspring.profiles.active=${SPRING_PROFILE} -jar zwt-repertory.jar