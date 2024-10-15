$(document).ready(function () {
  const ctx = document.getElementById("myDoughnutChart").getContext("2d");
  const myDoughnutChart = new Chart(ctx, {
    type: "doughnut",
    data: {
      labels: ["紅色", "藍色", "黃色"],
      datasets: [
        {
          label: "我的數據",
          data: [80, 20],
          backgroundColor: ["#5f46e8", "#e0e0e0"],
          borderColor: ["#5f46e8", "#e0e0e0"],
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      cutout: "95%", // 調整圓心的大小以使 doughnut 更細
      animation: {
        animateScale: true,
        animateRotate: true,
      },
      plugins: {
        // 在中間顯示文字
        tooltip: {
          enabled: false, // 禁用 tooltip
        },
        legend: {
          display: false, // 禁用圖例
        },
        // 自定義插件
        datalabels: {
          display: true,
          align: "center",
          anchor: "center",
          font: {
            size: "14",
            weight: "bold",
          },
          formatter: function () {
            return "200%"; // 在中間顯示的文本
          },
        },
      },
    },
    plugins: [
      {
        beforeDraw: function (chart) {
          const ctx = chart.ctx;
          const x = chart.width / 2;
          const y = chart.height / 2;
          ctx.restore();
          ctx.font = "bold 14px Arial";
          ctx.fillStyle = "#000"; // 文字顏色
          ctx.textAlign = "center";
          ctx.textBaseline = "middle";
          ctx.fillText("80%", x, y); // 中間的文字
          ctx.save();
        },
      },
    ],
  });
});
