# SistemDeStiri

Aplicatia implementeaza un sistem de stiri orientat pe evenimente. Un
eveniment este aparitia, modificarea sau stergerea unei stiri, iar
stirile sunt organizate in domenii si un numar arbitrar de subdomenii.
Stirile au si alte atribute cum ar fi: data primei publicari, data
ultimei modificari, sursa de informatie, autorul articolului etc.

Actorii din sistem sunt de doua tipuri: editori de stiri si cititori.
Editorii trebuie sa poata afla in timp real care este numarul de
cititori pentru stirile de interes. Pentru aceasta, ei se pot declara
interesati de aparitia unui eveniment gen "stire citita". Cititorii se
pot abona la una sau mai multe stiri, specificand domeniile de interes
si alte atribute (data, sursa etc.).

Documentatie : https://docs.oracle.com/javaee/5/tutorial/doc/bncdq.html
Topic guide : https://examples.javacodegeeks.com/enterprise-java/jms/jms-topic-example/
Download latest version ActiveMQ : https://activemq.apache.org/components/classic/download/
Integrare cu Intellij : File -> Project Structure -> Modules -> Dependencies -> + button -> JARs or directories -> activemq-all-5.16.0.jar
