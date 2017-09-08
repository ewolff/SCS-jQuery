SCS jQuery Sample
==============

[Deutsche Anleitung zum Starten des Beispiels](WIE-LAUFEN.md)

For [Self-contained Systems](http://scs-architecture.org) multiple
frontends need to be integrated. This sample shows how to do
this. JavaScript Code on the frontend is used to load parts from other
SCS. A hyperlink marked with class `embeddable`will be replaced with
the content found at that URL.

So for example `<a class="embeddable"
href="/catalog/2.snippet">item</a>` would be replaced by whatever a
GET to `/catalog/2.snippet`returns. You can find the code for this
in layout.html in
`microservice-demo/microservice-demo-order/src/main/resources/templates/layout.html` .

Varnish serves as a cache and also to provide both SCS at a common
URL. Note that this could also be done with a reverse proxy
e.g. Apache httpd or Nginx.

This project creates a Docker setup with the complete systems based on
Docker Compose and Docker Machine. The services are implemented in
Java using Spring and Spring Cloud.

It uses two very simple SCS:
- Order to enter orders.
- Catalog to handle the items in the catalog.

How To Run
----------

See [How to run](HOW-TO-RUN.md) for details.

Remarks on the Code
-------------------

The SCS are: 
- microservice-demo-catalog is the application to take care of
  items. You can access it at `/catalog`. Also it is used by order to
  display HTML snippets with the choice of items and the text for each
  item in an order.
- microservice-demo-order does order processing. It outputs
  `esi:include` in its HTML files to include the HTML snippets of
  microservices-demo-catalog. These are interpreted my the Varnish web
  cache.

Varnish provides both SCS at common URLs. The `default.vcl` in the directory
  `docker/varnish` includes the configuration to enable this. It
  defines two backends - one for each SCS. HTTP requests to `/catalog`
  are mapped to the catalog SCS while the default is the order
  SCS. The time to live for each item in the cache is 30s i.e. after
  30 seconds an entry in the cache in invalidated and the call goes to
  the backend. The grace is 15m: Even if the entry in the cache is
  invalid it will be served for 15 more minutes of the backend is not
  accessible. So if the backend crashes the cache can still provide
  some resilience.

The microservices have an Java main application in `src/test/java` to
run them stand alone with some test data.

Architecture Disclaimer
-------------------

This is a technology demo. The coupling between the two components in
this case is very tight - the integration is providing rather small
components that are embedded in specific pages. In a real world
architecture this should not be the case. Please refer to
http://scs-architecture.org/ to better understand the architecture
this should support.
