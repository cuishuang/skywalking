<#macro analysisResult>
<script type="text/x-jsrender" id="analysisResultPanelTmpl">
    <div class="row">
        <div class="col-md-4 ">
            <div class="input-group">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false"><span id="analyTypeDropDown">Action</span><span
                            class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0);" name="analyTypeDropDownOption" value="HOUR">时报表</a></li>
                        <li><a href="javascript:void(0);" name="analyTypeDropDownOption" value="DAY">日报表</a></li>
                        <li><a href="javascript:void(0);" name="analyTypeDropDownOption" value="MONTH">月报表</a></li>
                    </ul>
                </div>
                <input type="text" class="form-control" readonly id="analyDate">
            <span class="input-group-btn">
              <button class="btn btn-default" type="button" id="showAnalyResultBtn">Go!</button>
            </span>
            </div>
        </div>
        <div class="col-md-4 col-md-offset-4">
            <span><a href="javascript:void(0);" id="previousHourBtn">上个小时</a></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span><a href="javascript:void(0);" id="yesterdayBtn">昨天</a></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span><a href="javascript:void(0);" id="currentMonthBtn">本月</a></span>&nbsp;&nbsp;&nbsp;&nbsp;
            <span><a href="javascript:void(0);" id="previousMonthBtn">上月</a></span>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
    </div>
    <hr/>
    <div class="row">
            <input type="hidden" id="treeId" value="{{>treeId}}"/>
            <input type="hidden" id="analyType" value=""/>
            <table class="gridtable">
                <thead>
                <tr>
                    <th width="10%">LevelId</th>
                    <th width="62%">ViewPoint</th>
                    <th width="7%">调用次数</th>
                    <th width="7%">正确次数</th>
                    <th width="5%">正确率</th>
                    <th width="7%">平均耗时</th>
                </tr>
                </thead>
                <tbody id="dataBody">
                </tbody>
            </table>
    </div>
</script>
</#macro>

<#macro analysisResultTableTmpl>
<script type="text/x-jsrender" id="analysisResultTableTmpl">
        <tr id="a">
            {{if isPrintLevelId}}
                <td rowspan="{{>rowSpanCount}}" valign="middle">{{>traceLevelId}}</td>
            {{/if}}
            <td><a href="#">{{>viewPoint}}<a></td>
            <td>{{>anlyResult.totalCall}}</td>
            <td>{{>anlyResult.correctNumber}}</td>
            <td>
            <span class="
         {{if anlyResult.correctRate >= 99.00}}
         text-success
         {{else anlyResult.correctRate >= 97}}
         text-warning
         {{else}}
         text-danger
         {{/if}}
         ">
            <strong>{{>anlyResult.correctRate}}%</strong></span></td>
            <td>{{>anlyResult.averageCost}}ms</td>
        </tr>

</script>
</#macro>