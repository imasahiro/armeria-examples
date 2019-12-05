package example.armeria.server.annotated;

import org.junit.Test;

import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.Endpoint;
import com.linecorp.armeria.client.endpoint.EndpointGroup;
import com.linecorp.armeria.client.endpoint.healthcheck.HealthCheckedEndpointGroup;

/**
 * Tests to check {@link HealthCheckedEndpointGroup} behavior.
 * Please run `./gradlew annotated-http-service:run` before execute this test.
 */
public class HCTest {
    private ClientFactory clientFactory = ClientFactory.builder().build();
    private HealthCheckedEndpointGroup e;

    @Test
    public void endpointClose() throws Exception {
        e = create();
        e.close();
        clientFactory.close();
    }

    @Test
    public void clientFactoryClose() throws Exception {
        e = create();
        clientFactory.close();
    }

    @Test
    public void noClose() throws Exception {
        e = create();
        clientFactory.close();
    }

    HealthCheckedEndpointGroup create() throws Exception {
        final HealthCheckedEndpointGroup e = HealthCheckedEndpointGroup
                .builder(EndpointGroup.of(Endpoint.of("127.0.0.1", 8080)), "/health")
                .clientFactory(clientFactory)
                .build();
        e.awaitInitialEndpoints();
        Thread.sleep(100);
        return e;
    }
}
