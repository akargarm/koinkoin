<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Welcome to the KOIN Marketplace!</title>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-xs-12 col-xs-offset-2">
                    <h3>Register for KOIN!</h3>
                    <form class="form-horizontal" id="transfer" action="RegisterServlet" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="fName" class="col-sm-2 control-label">First name</label>
                            <div class="col-sm-3 input-group">
                                <span class="input-group-addon glyphicon glyphicon-bitcoin"></span>
                                <input class="form-control" id="fName" type="text" name="fName" required placeholder="Enter first name"><br>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lName" class="col-sm-2 control-label">Last name</label>
                            <div class="col-sm-3 input-group">
                                <span class="input-group-addon glyphicon glyphicon-bitcoin"></span>
                                <input class="form-control" id="lName" type="text" name="lName" required placeholder="Enter last name"><br>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="username" class="col-sm-2 control-label">Username</label>
                            <div class="col-sm-3 input-group">
                                <span class="input-group-addon glyphicon glyphicon-bitcoin"></span>
                                <input class="form-control" id="username" type="text" name="username" required placeholder="Enter username"><br>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="key" class="col-sm-2 control-label">Keystore File</label>
                            <div class="col-sm-3 input-group">
                                <span class="input-group-addon glyphicon glyphicon-file"></span>
                                <input class="form-control" id="key" type="file" name="key"><br>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="col-sm-2 control-label">Password</label>
                            <div class="col-sm-3 input-group">
                                <span class="input-group-addon glyphicon glyphicon-lock"></span>
                                <input class="form-control" id="password" type="password" name="password" required placeholder="Enter password"><br>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-10 col-sm-push-2">
                                <input class="btn btn-info" type="submit" name="submit" value="Submit">
                            </div>
                        </div>
                    </form>
                    ${symbol}
                </div>
            </div>
        </div>
    </body>
</html>