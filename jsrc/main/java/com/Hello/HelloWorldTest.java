package com.Hello;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.HashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "hello_world-test",
	roleName = "hello_world-test-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorldTest implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final int SC_OK = 200;
	private static final int SC_BAD_REQUEST = 400;
	private final Gson gson = new Gson();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
		context.getLogger().log(apiGatewayProxyRequestEvent.toString());
		Map<String, String> queryStringParameters = apiGatewayProxyRequestEvent.getQueryStringParameters();
		try {
			String responseBody = gson.toJson(new HelloWorldTest.ResponseObject(SC_OK, "Hello from Lambda"));
			return new APIGatewayProxyResponseEvent()
					.withStatusCode(SC_OK)
					.withBody(responseBody);
		} catch (Exception exception) {
			String responseBody = gson.toJson(new HelloWorldTest.ResponseObject(SC_BAD_REQUEST, "Error"));
			return new APIGatewayProxyResponseEvent()
					.withStatusCode(SC_BAD_REQUEST)
					.withBody(responseBody);
		}

	}

	private static class ResponseObject {
		private int statusCode;
		private String message;

		public ResponseObject(int statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
