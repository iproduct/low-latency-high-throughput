<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>IPT Polling Demo MVC 1.0 | Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="css/custom.css">
        <style>
        </style>
    </head>
    <body>
        <!-- Enable HTML 5 elements in IE 7+8 -->
        <!--[if lt IE 9]>
           <script src="js/html5shiv.min.js"></script>
        <![endif]-->
        <div class="container">

            <!-- Horizontal navigation -->
            <div class="navbar-header">

                <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">
                    <img alt="IPT Course Web Design 2015" src="img/design.png">
                </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="navbar-collapse bs-navbar-collapse collapse in" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
                    <li><a href="#">Link</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">Dropdown <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#">Action</a></li>
                            <li><a href="#">Another action</a></li>
                            <li><a href="#">Something else here</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a></li>
                            <li class="divider"></li>
                            <li><a href="#">One more separated link</a></li>
                        </ul>
                    </li>
                </ul>
                <form class="navbar-form navbar-left" role="search">
                    <div class="form-group">
                        <input type="text" class="form-control" placeholder="Search">
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#">Link</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Dropdown <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#">Action</a></li>
                            <li><a href="#">Another action</a></li>
                            <li><a href="#">Something else here</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a></li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->

            <div class="jumbotron">
                <h1>Hello from HTML 5</h1>
                <p>HTML 5 is a core technology markup language of the Internet used for 
                    structuring and presenting content for the World Wide Web. As of 
                    October 2014 this is the final and complete[2] fifth revision of 
                    the HTML standard of the 
                    <a href="http://w3.org">World Wide Web Consortium (W3C)</a>
                    .[3] The 
                    previous version, HTML 4, was standardised in 1997.
                </p>
                <a href="demos.html" class="btn btn-primary btn-lg">
                    See More Demos ...
                </a>
            </div>

            <div class="row">
                <section class="js_demos col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <article id="init_demo">
                        <h2>Init function demo</h2>
                        <img class="img-rounded img-responsive" src="img/world.jpg" alt="world picture">
                        <div class="output"></div>
                        <div class="output"></div>
                        <div class="output"></div>
                        <button id="hide_all">Hide all texts</button>
                    </article>
                </section>    
                <section class="js_demos col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <article id="init_demo">
                        <h2>Init function demo</h2>
                        <div class="embed-responsive embed-responsive-4by3">
                            <iframe class="embed-responsive-item" 
                                    src="https://www.youtube.com/embed/XSGBVzeBUbk" 
                                    frameborder="0" allowfullscreen>                          
                            </iframe>
                        </div>
                        <div class="output"></div>
                        <div class="output"></div>
                        <div class="output"></div>
                        <button id="hide_all">Hide all texts</button>
                    </article>
                </section>    
                <section class="js_demos col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <article id="init_demo">
                        <h2>Init function demo</h2>
                        <div class="output"></div>
                        <div class="output"></div>
                        <div class="output"></div>
                        <button id="hide_all">Hide all texts</button>
                    </article>
                </section>    
                <section class="js_demos col-xs-12 col-sm-6 col-md-12 col-lg-3">
                    <article id="init_demo">
                        <h2>Init function demo</h2>
                        <div class="output"></div>
                        <div class="output"></div>
                        <div class="output"></div>
                        <button id="hide_all">Hide all texts</button>
                    </article>
                </section>    
            </div>  <!-- DIV class="row" -->

            <!-- Image Gallery -->
            <div class="row">
                <h2 class="col-lg-12">Recent Images</h2>
                <div class="col-md-4">
                    <a href="img/frozen.jpg" class="thumbnail img-rounded">
                        <p>Pulpit Rock: A famous tourist attraction in Forsand, Ryfylke, Norway.</p> 
                        <img src="img/frozen.jpg" alt="Pulpit Rock" style="width:150px;height:150px">
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="img/frozen.jpg" class="thumbnail img-rounded">
                        <p>Moustiers-Sainte-Marie: Considered as one of the "most beautiful villages of France".</p>
                        <img src="img/world-atlas.jpg" alt="Moustiers Sainte Marie" style="width:150px;height:150px">
                    </a>
                </div>
                <div class="col-md-4">
                    <a href="img/world.jpg" class="thumbnail img-rounded">
                        <p>The Cinque Terre: A rugged portion of coast in the Liguria region of Italy.</p> 
                        <img src="img/world.jpg" alt="Cinque Terre" style="width:150px;height:150px">
                    </a>
                </div>
            </div> <!-- DIV class="row" -->
        </div>

        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

        <!-- Latest compiled JavaScript -->
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <script src="js/custom.js" type="text/javascript"></script>
    </body>
</html>

