var express=require("express");
var mongo=require("mongodb").MongoClient;
var bodyParser=require("body-parser");
var assert=require("assert");
var session=require("express-session");
var multer=require("multer");
var distance=require("google-distance");
var port=3000;
var app=express();
var upload=multer({dest:"assets/uploads/"});
var photo="assets/uploads/";
var sess;
var urlpost=bodyParser.urlencoded({extended:false});
app.use(session({secret:"yash",resave:false,saveUninitialized:true}));
app.use("/assets",express.static("assets"));
var url="mongodb://localhost:27017/mp";
app.set("view engine","ejs");

app.get("/distance",function(req,res){
  distance.get(
{
  origins: ['Sangli'],
  destinations: ['Nagpur']
},
function(err, data) {
  if (err)
  return console.log(err);
  res.send(data);
});
});

app.get("/",function(req,res){
  res.sendFile(__dirname+"/index.html");
});

app.post("/register",upload.single("file"),function(req,res){
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    db.collection("users").insert({"username":req.body.username,"email":req.body.email,"password":req.body.password,"image":photo+req.file.filename,"status":0});
    db.close();
  });
  res.sendFile(__dirname+"/index.html");
});

app.get("/sign-up",function(req,res){
  res.sendFile(__dirname+"/register.html");
});

app.post("/login",urlpost,function(req,res){
  var result=[];
  sess=req.session;
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({$or:[{"username":req.body.username},{"email":req.body.username}],"password":req.body.password});
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
      if(result.length==0)
      res.sendFile(__dirname+"/index.html");
      else {
        sess.username=result[0]["username"];
        if(result[0]["status"]==0)
        res.render("student-form-fill",{data:result});
        else if(result[0]["status"]==1)
        res.render("student-dashboard",{data:result});
        else if(result[0]["status"]==1000)
        res.render("admin-dashboard",{data:result});
      }
    });
  });
});

app.post("/student-details",urlpost,function(req,res){
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    db.collection("users").update({"username":sess.username},{$set:{"name":req.body.name,"contact":req.body.contact,"gender":(req.body.gender=='true'),"category":(req.body.category=='true'),"city":req.body.city,"status":1}});
    db.close();
  });
  res.send("Success");
});

app.get("/logout",function(req,res){
  req.session.destroy();
  res.sendFile(__dirname+"/index.html");
});

app.get("/admin-add-parameter",function(req,res){
  var result=[];
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({"username":sess.username});
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
      res.render("admin-add-parameter",{data:result});
    });
  });
});

app.post("/admin-add-parameter",urlpost,function(req,res){
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    //db.collection("factorsettings").insert({"Number_of_Factors":7,"Number_of_Optional_Factors":3});
    db.collection("factorsettings").update({"Number_of_Factors":7},{$push:{"Factors":{"name":req.body.parameter,"DefaultWeight":req.body.weight,"DefaultIndex":req.body.index}}});
    db.close();
  });
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({"username":sess.username});
    var result=[];
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
      res.render("admin-dashboard",{data:result});
    });
  });
});

app.get("/get-parameter",function(req,res){
  var result=[];
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("factorsettings").find();
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
    });
  });
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({"username":sess.username});
    var myresult=[];
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      myresult.push(doc);
    },function(){
      db.close();
      res.render("get-parameter",{data:result,mydata:myresult});
    });
  });
});
app.get("/add-notice",function(req,res){
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({"username":sess.username});
    var result=[];
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
      res.render("add-notice",{data:result});
    });
  });
});

app.post("/add-notice",urlpost,function(req,res){
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    db.collection("notice").insert({"notice":req.body.notice});
    db.close();
  });
  mongo.connect(url,function(err,db){
    assert.equal(null,err);
    var cursor=db.collection("users").find({"username":sess.username});
    var result=[];
    cursor.forEach(function(doc,err){
      assert.equal(null,err);
      result.push(doc);
    },function(){
      db.close();
      res.render("admin-dashboard",{data:result});
    });
  });
});
console.log("Runnning on port : "+port);
app.listen(port);
