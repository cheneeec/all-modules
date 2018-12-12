FROM openjdk:11.0-jre-slim

COPY ./target/j-video-web.jar /usr/src/java-app/
WORKDIR /usr/src/java-app
#
ENV PROFILE default
ENV MONGO_HOST 127.0.0.1
ENV REDIS_HOST 127.0.0.1
ENV APP_PROXY false
ENV PORT 8080

EXPOSE ${PORT}

ENTRYPOINT java -jar j-video-web.jar  --spring.profiles.active=${PROFILE} \
                                      --spring.redis.host=${REDIS_HOST} \
                                      --spring.data.mongodb.host=${MONGO_HOST} \
                                      --app.proxy_pool.enable=${APP_PROXY} \
                                      --server.port=${PORT} \
                                      --app.spider.enable=true