python google-cloud-sdk\bin\dev_appserver.py "C:\Users\Gokul Manoj\Desktop\app\ae-01-trivial"


application: ae-01-trivial
version: 1
runtime: python
api_version: 1
handlers:
- url: /.*
  #script: index.py
  upload: index.html
  static_files: index.html