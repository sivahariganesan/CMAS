<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>CMAS</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.css"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            body
            {
                font-family:"Trebuchet MS",Helvetica,sans-serif;
                overflow:hidden;
            }
         </style>
    </head>
    <body>
        <div class="col-md-12" style="background-color:#60a1c6;color:white;margin-top: 1%;padding-top:0.5%;">
            <ul class="nav nav-tabs col-sm-offset-6" style="padding-top:0.3%;">
                <li role="presentation" class="active"><a>Report Crisis</a></li>
            </ul>
        </div>
        <br/>
        <br/>
        <form action="CrisisProcessor.do?method=reportCrisis" method="POST">
            <div class="row">
                <div class="col-md-10 col-md-offset-3">                
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Pin Code:</label>
                        <div class="col-sm-4">
                            <input type="text" id="pincode" name="pincode" class="form-control" placeholder="Pincode of crisis area" >
                        </div>
                    </div>                 
                </div>                
            </div>
            <br/>
            <div class="row">
                <div class="col-md-10 col-md-offset-3">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Crisis:</label>
                        <div class="col-sm-4">
                            <textarea placeholder="Enter Crisis in less than 1000 characters" id="crisis" class="form-control" name="crisis"></textarea>
                        </div>
                    </div> 
                 </div>
            </div> 
            <br/>
            <div class="form-group">
                <div class="col-md-offset-6 col-md-8">
                  <button type="submit" style="float:left;" class="btn btn-info active" id="sendCrisis">Send</button>              
                </div>
            </div>
        </form>
		<div class="modal fade bs-example-modal-sm" id="crisisStatus" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">Crisis Status</h4>
					</div>
					<div class="modal-body">
						<p id="crisisStatusMsg"></p>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script  src="bootstrap/js/jquery-1.11.0.min.js"></script>
	<script src="bootstrap/js/bootstrap.js"></script>
	<script>
		var pincode=$("#pincode").val();
		var crisis=$("#crisis").val();
		$.ajax({
			type: "POST",
			dataType: 'html',
			url:"GetDiff",
			data:{pincode:pincode,crisis:crisis},
			success:function(data){
				
			}
</html>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>