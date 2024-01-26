FROM openjdk:17
EXPOSE 9000
ADD target/razorpay-0.0.1-SNAPSHOT.jar razorpay-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java","-jar", "/razorpay-0.0.1-SNAPSHOT.jar"]