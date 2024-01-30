package com.github.cryptojuenger.template.infrastructure.hcloudprovider.hcloud.data_hcloud_load_balancer;

@javax.annotation.Generated(value = "jsii-pacmak/1.93.0 (build 1706ca5)", date = "2024-01-28T13:37:41.289Z")
@software.amazon.jsii.Jsii(module = com.github.cryptojuenger.template.infrastructure.hcloudprovider.hcloud.$Module.class, fqn = "hcloud.dataHcloudLoadBalancer.DataHcloudLoadBalancerServiceHealthCheckHttp")
@software.amazon.jsii.Jsii.Proxy(DataHcloudLoadBalancerServiceHealthCheckHttp.Jsii$Proxy.class)
public interface DataHcloudLoadBalancerServiceHealthCheckHttp extends software.amazon.jsii.JsiiSerializable {

    /**
     * @return a {@link Builder} of {@link DataHcloudLoadBalancerServiceHealthCheckHttp}
     */
    static Builder builder() {
        return new Builder();
    }
    /**
     * A builder for {@link DataHcloudLoadBalancerServiceHealthCheckHttp}
     */
    public static final class Builder implements software.amazon.jsii.Builder<DataHcloudLoadBalancerServiceHealthCheckHttp> {

        /**
         * Builds the configured instance.
         * @return a new instance of {@link DataHcloudLoadBalancerServiceHealthCheckHttp}
         * @throws NullPointerException if any required attribute was not provided
         */
        @Override
        public DataHcloudLoadBalancerServiceHealthCheckHttp build() {
            return new Jsii$Proxy(this);
        }
    }

    /**
     * An implementation for {@link DataHcloudLoadBalancerServiceHealthCheckHttp}
     */
    @software.amazon.jsii.Internal
    final class Jsii$Proxy extends software.amazon.jsii.JsiiObject implements DataHcloudLoadBalancerServiceHealthCheckHttp {

        /**
         * Constructor that initializes the object based on values retrieved from the JsiiObject.
         * @param objRef Reference to the JSII managed object.
         */
        protected Jsii$Proxy(final software.amazon.jsii.JsiiObjectRef objRef) {
            super(objRef);
        }

        /**
         * Constructor that initializes the object based on literal property values passed by the {@link Builder}.
         */
        protected Jsii$Proxy(final Builder builder) {
            super(software.amazon.jsii.JsiiObject.InitializationMode.JSII);
        }

        @Override
        @software.amazon.jsii.Internal
        public com.fasterxml.jackson.databind.JsonNode $jsii$toJson() {
            final com.fasterxml.jackson.databind.ObjectMapper om = software.amazon.jsii.JsiiObjectMapper.INSTANCE;
            final com.fasterxml.jackson.databind.node.ObjectNode data = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();


            final com.fasterxml.jackson.databind.node.ObjectNode struct = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();
            struct.set("fqn", om.valueToTree("hcloud.dataHcloudLoadBalancer.DataHcloudLoadBalancerServiceHealthCheckHttp"));
            struct.set("data", data);

            final com.fasterxml.jackson.databind.node.ObjectNode obj = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode();
            obj.set("$jsii.struct", struct);

            return obj;
        }
    }
}