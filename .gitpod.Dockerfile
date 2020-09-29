FROM gitpod/workspace-full

RUN git clone https://github.com/spring-petclinic/spring-petclinic-angular.git
RUN cd spring-petclinic-angular
RUN npm uninstall -g angular-cli @angular/cli
RUN npm install -g @angular/cli@8.0.3
RUN npm install --save-dev @angular/cli@8.0.3
RUN npm install --save-dev @angular-devkit/build-angular
RUN npm install -g typescript
RUN npm install
RUN npm run build
