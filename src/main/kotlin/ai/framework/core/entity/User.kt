package ai.framework.core.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class User(
        val name: String = "",
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val sharedKey: String = "",
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val endpoint: String = "",
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val endpointCredentials: String = "")