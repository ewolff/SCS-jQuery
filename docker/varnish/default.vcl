vcl 4.0;

backend default {
    .host = "order";
    .port = "8080";
}

backend catalog {
    .host = "catalog";
    .port = "8080";
}

probe default {
    .url = "/";
    .timeout = 1s;
    .interval = 2s;
    .window = 3;
    .threshold = 2;
}

sub vcl_recv {
    if (req.url ~ "^/catalog") {
       set req.url = regsub(req.url, "^/catalog", "");
       set req.backend_hint = catalog;
    }
}

sub vcl_backend_response {
    set beresp.ttl = 30s;
    set beresp.grace = 15m;
}