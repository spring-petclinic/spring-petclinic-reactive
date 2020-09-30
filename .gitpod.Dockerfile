FROM gitpod/workspace-full

RUN npm uninstall angular-cli @angular/cli
RUN npm install @angular/cli@8.0.3

# Install building dependencies
RUN npm install --save-dev @angular/cli@8.0.3
RUN npm install --save-dev @angular-devkit/build-angular