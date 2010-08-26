<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
<head>
    <title>Java TopCoder Aggregator</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="css/project-style.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="css/datatables-jquery.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="css/smoothness/jquery-ui-1.8.2.custom.css"/>"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<c:url value="js/jquery.corner.js"/>"></script>
    <script type="text/javascript" src="<c:url value="js/jquery.dataTables.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="js/project-main.js"/>"></script>
    <script type="text/javascript">
        $('.corner').corner();
        $(".corner-bottom").corner("bottom");

/*
        $("#dialog-modal").dialog({
			height: 140,
			modal: true
		});
*/
    </script>
</head>
<body>
<center>
    <div id="dialog" style="display: none;" title="Dialog title">
        <p>
            <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
        </p>
    </div>

    <div id="content">

        <div id="top" class="corner">
            <div id="top_left">
                <div id="logo_text">
                    <a href="index.jsp">Java TopCoder Aggregator</a>
                </div>
                <div id="logo_quote">
                    Simplified access to the <a class="reverse" href="http://www.topcoder.com/tc">TopCoder</a> Java contests.
                </div>
            </div>

            <div id="top_right" class="corner">
                <input type="text" id="subscriberMail">
                <button type="submit" id="submitButton" onclick="subscribe()"/>
                <label for="submitButton">Subscribe</label>
            </div>
        </div>

        <div id="main">
            <div id="tabs">
                <ul>
                    <li><a href="#tab-1"><span>Component Development</span></a></li>
<%--
                    <li><a href="#tab-2"><span>Component Design</span></a></li>
--%>
                </ul>
                <div id="tab-1">
                    <form>
                        <div id="radio">
<%--
                            <input type="radio" id="radioPast" name="radio"/>
                            <label for="radioPast">Past</label>
--%>
                            <input type="radio" id="radioActive" name="radio" checked="checked" onclick="activateCdevType(ContestsConfig.CONTESTS_TYPE_ACTIVE);"/>
                            <label for="radioActive">Active</label>
                            <input type="radio" id="radioUpcoming" name="radio" onclick="activateCdevType(ContestsConfig.CONTESTS_TYPE_UPCOMING);"/>
                            <label for="radioUpcoming">Upcoming</label>
<%--
                            <input type="radio" id="radioAll" name="radio" onclick="activateCdevType(ContestsConfig.CONTESTS_TYPE_ALL);"/>
                            <label for="radioAll">All</label>
--%>
                        </div>
                    </form>

                    <table cellpadding="0" cellspacing="0" border="0" class="display" id="cdevtable">
                        <thead>
                        <tr>
                            <th width="30%">Component</th>
                            <th width="10%">Register by</th>
                            <th width="10%">Submit by</th>
                            <th width="10%">Payment</th>
                            <th width="10%">Reliability Bonus</th>
                            <th width="10%">DR Points</th>
                            <th width="10%">Registrants</th>
                            <th width="10%">Submissions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan="8" class="dataTables_empty">Loading data from server</td>
                        </tr>
                        </tbody>
                        <%--
                            <tfoot>
                                <tr>
                                    <th>Rendering engine</th>
                                    <th>Browser</th>
                                </tr>
                            </tfoot>
                        --%>
                    </table>
                </div>
<%--
                <div id="tab-2">
                </div>
--%>
            </div>
        </div>

        <div id="footer" class="corner" style="float:left; margin-left:250px;">
            Source code is freely available under GPLv3 at <a href="http://github.com/shyiko/jtcaggr">http://github.com/shyiko/jtcaggr</a>.
            <br>
            <span id="copyright">
                Copyright &copy; 2010 <a href="http://ua.linkedin.com/in/shyiko">Stanley Shyiko</a>
            </span>
        </div>

        <div style="float:right; padding-top:16px;">
            <a style="color:white;" href="http://code.google.com/appengine"/>
            <img src="http://code.google.com/appengine/images/appengine-noborder-120x30.gif"
                 alt="Powered by Google App Engine"/>
            <a/>
        </div>
    </div>
</center>
</body>
</html>
