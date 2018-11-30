if (typeof $ == 'undefined'){
	$ = {};
}
$.parameter = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function binayUtf8ToString(buf, begin){
	  var i = 0;
	  var pos = 0;
	  var str ="";
	  var unicode = 0 ;
	  var flag = 0;
	  for (pos = begin; pos < buf.length;){
	    flag= buf[pos];
	    if ((flag >>>7) === 0 ) {
	      str+= String.fromCharCode(buf[pos]);
	      pos += 1;
	      
	    }
	    else if ((flag &0xFC) === 0xFC ){
	      unicode = (buf[pos] & 0x3) << 30;
	      unicode |= (buf[pos+1] & 0x3F) << 24; 
	      unicode |= (buf[pos+2] & 0x3F) << 18; 
	      unicode |= (buf[pos+3] & 0x3F) << 12; 
	      unicode |= (buf[pos+4] & 0x3F) << 6;
	      unicode |= (buf[pos+5] & 0x3F);
	      str+= String.fromCharCode(unicode) ;
	      pos += 6;
	      
	    }else if ((flag &0xF8) === 0xF8 ){
	      unicode = (buf[pos] & 0x7) << 24;
	      unicode |= (buf[pos+1] & 0x3F) << 18; 
	      unicode |= (buf[pos+2] & 0x3F) << 12; 
	      unicode |= (buf[pos+3] & 0x3F) << 6;
	      unicode |= (buf[pos+4] & 0x3F);
	      str+= String.fromCharCode(unicode) ;
	      pos += 5;
	      
	    }
	    else if ((flag &0xF0) === 0xF0 ){
	      unicode = (buf[pos] & 0xF) << 18;
	      unicode |= (buf[pos+1] & 0x3F) << 12; 
	      unicode |= (buf[pos+2] & 0x3F) << 6;
	      unicode |= (buf[pos+3] & 0x3F);
	      str+= String.fromCharCode(unicode) ;
	      pos += 4;
	      
	    }
	    
	    else if ((flag &0xE0) === 0xE0 ){
	      unicode = (buf[pos] & 0x1F) << 12;;
	      unicode |= (buf[pos+1] & 0x3F) << 6;
	      unicode |= (buf[pos+2] & 0x3F);
	      str+= String.fromCharCode(unicode) ;
	      pos += 3;
	      
	    }
	    else if ((flag &0xC0) === 0xC0 ){ //110
	      unicode = (buf[pos] & 0x3F) << 6;
	      unicode |= (buf[pos+1] & 0x3F);
	      str+= String.fromCharCode(unicode) ;
	      pos += 2;
	      
	    }
	    else{
	      str+= String.fromCharCode(buf[pos]);
	      pos += 1;
	    }
	 } 
	 return str;
	  
	}
function byteToString(arr) {  
    if(typeof arr === 'string') {  
        return arr;  
    }  
    var str = '',  
        _arr = arr;  
    for(var i = 0; i < _arr.length; i++) {  
        var one = _arr[i].toString(2),  
            v = one.match(/^1+?(?=0)/);  
        if(v && one.length == 8) {  
            var bytesLength = v[0].length;  
            var store = _arr[i].toString(2).slice(7 - bytesLength);  
            for(var st = 1; st < bytesLength; st++) {  
                store += _arr[st + i].toString(2).slice(2);  
            }  
            str += String.fromCharCode(parseInt(store, 2));  
            i += bytesLength - 1;  
        } else {  
            str += String.fromCharCode(_arr[i]);  
        }  
    }  
    return str;  
}  
$.ws = {
	toFrame : function(frameRaw) {//如果是二进制侦则解析
		// debugger;
		var up = 0;
		var down = 0;
		var field = 0;// 0=heads;1=params;2=content
		var frame = {
			heads : {},
			params : {}
		};
		while (down < frameRaw.length) {
			if(field<2){//参见frame.java
				if (frameRaw[up] == '\r'
						&& (up + 1 < frameRaw.length && frameRaw[up + 1] == '\n')) {// 跳域
					field++;
					up += 2;
					down += 2;
					continue;
				}
			}else {
				down = frameRaw.length;
				var content = frameRaw.substr(up, down - up);
				frame.content = content;
				break;
			}
			if (frameRaw[down] == '\r'
					&& (down + 1 < frameRaw.length && frameRaw[down + 1] == '\n')) {// 跳行
				var b = frameRaw.substr(up, down - up);
				switch (field) {
				case 0:
					var kv = b;
					var at = kv.indexOf("=");
					var k = kv.substr(0, at);
					var v = kv.substr(at + 1, kv.length);
					frame.heads[k] = v;
					break;
				case 1:
					kv = b;
					at = kv.indexOf("=");
					k = kv.substr(0, at);
					v = kv.substr(at + 1, kv.length);
					frame.params[k] = v;
					break;
				}
				down += 2;
				up = down;
				continue;
			}
			down++;
		}
		return frame;
	},
	open : function(wsurl, onmessage, onopen, onclose,onerror) {
		var doReceive = function(e) {
			var raw=e.data;
			if( raw instanceof ArrayBuffer){
				 raw = new Int8Array(raw);
				 raw=binayUtf8ToString(raw,0);
			}
			var frame = $.ws.toFrame(raw);
			var status=parseInt(frame.status);
			if(status>=300){
				alert(frame.message);
				return;
			}
			if('frame/bin'!=frame.heads['Content-Type']&&'frame/json'!=frame.heads['Content-Type']){
				if (typeof onmessage != 'undefined' && onmessage != null) {
					onmessage(frame);
				}
				return;
			}
			var len=parseInt(frame.heads["Content-Length"]);
			if(len<1){
				return;
			}
			frame=$.ws.toFrame(frame.content);
			if (typeof onmessage != 'undefined' && onmessage != null) {
				onmessage(frame);
			}
		}
		
		var websocket = {
			ws : wsurl,
			init:function(){
				var socket;
				if (!window.WebSocket) {
					window.WebSocket = window.MozWebSocket;
				}
				if (window.WebSocket) {
					socket = new WebSocket(wsurl);
					// Setting binaryType to accept received binary as either 'blob' or 'arraybuffer'
					socket.binaryType = 'arraybuffer';
					socket.onmessage = doReceive;
					socket.onopen = onopen;
					socket.onerror=onerror;
					socket.onclose = onclose;
				} else {
					alert("Your browser does not support Web Socket.\r\n please download newrest browser version or download chrome or firbox .");
					return;
				}
				this.socket=socket;
			},
			send : function(frame) {
				if (!window.WebSocket) {//将来添加comet技术，以模拟websocket的api
					return;
				}
				var rule=/^protocol\s*=\s*(\S+)$/mg;
				var groups=rule.exec(frame);
				if(groups.length<1){
					alert('不是侦格式');
					return;
				}
				var socket = this.socket;
				if (socket.readyState == WebSocket.OPEN) {
					socket.send(frame);
				} else {
					alert("The socket is not open.");
				}
			},
			close : function() {
				socket.close();
			}
		};
		websocket.init();
		return websocket;
	}
}
