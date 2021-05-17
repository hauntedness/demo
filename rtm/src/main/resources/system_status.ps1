$totalRam = (Get-CimInstance Win32_PhysicalMemory | Measure-Object -Property capacity -Sum).Sum
$cpuTime = 100 - (Get-Counter '\Processor(_Total)\% Processor Time').CounterSamples.CookedValue
$availRam = (Get-Counter '\Memory\Available MBytes').CounterSamples.CookedValue
$summary = New-Object System.Collections.ArrayList
[Void]$summary.Add('CPU available: ' + $cpuTime.ToString('#,0.00') + '%')
[Void]$summary.Add('RAM available: ' + (104857600 * $availRam / $totalRam).ToString('#,0.00') + '%')
$disks = Get-CimInstance -ClassName Win32_logicalDisk | Select-Object -Property DeviceID,Size,FreeSpace
foreach($i in [System.Collections.ArrayList]$disks){
 [Void]$summary.Add('' + $i.DeviceID + '  available: ' + (100*$i.FreeSpace/$i.Size).ToString('#,0.00') + '%')
}
$ReadIO = (Get-Counter '\Process(*chia*)\IO Read Bytes/sec').CounterSamples.CookedValue | % {$a = 0} {$a += $_} {($a/1024/1024).ToString('#,0.00')} 
[Void]$summary.Add('IO  Read     : ' + $ReadIO + ' MB/s')
$WriteIO = (Get-Counter '\Process(*chia*)\IO Write Bytes/sec').CounterSamples.CookedValue | % {$a = 0} {$a += $_} {($a/1024/1024).ToString('#,0.00')} 
[Void]$summary.Add('IO  Write    : ' + $WriteIO + ' MB/s')
$chia_count = @(Get-Process | ?{ $_.ProcessName -eq 'Chia'}).Count.ToString() + ' running'
[Void]$summary.Add('exe Chia     : ' + $chia_count)
$hpool_count =  @(Get-Process '*Hpool*').Count.ToString() + ' running'
[Void]$summary.Add('exe hpool    : ' + $hpool_count)
Write-Output $summary