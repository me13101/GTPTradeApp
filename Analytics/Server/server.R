library("websockets")
library("caTools")
library("quantmod")
httpd = function(socket, header) {
  body = "<html><body><form>Ticker: <input type='text' name='symbol'/></form>"
  vars = http_vars(socket, header)
  if(!is.null(vars)) {
    getSymbols(vars$symbol)
    f = tempfile()
    jpeg(file=f, quality=100, width=650, heigh=450)
    chartSeries(get(vars$symbol), name=vars$symbol, TA=c(addVo(),addBBands()))
    dev.off()
    img = base64encode(readBin(f,what="raw",n=1e6))
    unlink(f)
    body = paste(body,"<br/><img src='data:image/jpg;base64,\n", img,"'</img>")
  }
  http_response(socket,content=charToRaw(paste(body,"</body></html")))
}
w = create_server(webpage=httpd, port=9999)
cat("Direct your browser to http://localhost:9999\n")
while(TRUE) service(w)
