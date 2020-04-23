package com.yanyiyun.baseutils.library.function.treeListView;

import com.yanyiyun.baseutils.library.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TreeHelper {


    /**
     * 传入我们的普通bean，转化为我们排序后的Node
     * @param datas
     * @param defaultExpandLevel  默认的展开层数
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas,
                                                int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException
    {
        List<Node> result = new ArrayList<Node>();
        //将用户数据转化为List<Node>以及设置Node间关系
        List<Node> nodes = convetData2Node(datas);
        //拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        //排序
        for (Node node : rootNodes)
        {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出可见的节点
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes){
        List<Node> result=new ArrayList<>();

        for(Node node:nodes){
            if(node.isRoot()||node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }
        }

        return result;
    }

    /**
     *
     * @param result
     * @param node
     * @param defaultExpandLevel   默认的展开层数
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if(defaultExpandLevel>=currentLevel){
            node.setExpand(true);
        }

        if(node.isLeaf()){
            return;
        }

        for(int i=0;i<node.getChildren().size();i++){
            addNode(result,node.getChildren().get(i),defaultExpandLevel,currentLevel+1);
        }
    }

    /**
     * 获取根节点列表
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root=new ArrayList<>();
        for(Node node:nodes){
            if(node.isRoot()){
                root.add(node);
            }
        }
        return root;
    }


    /**
     * 将我们的数据转化为树的节点
     *
     * @param datas
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static <T> List<Node> convetData2Node(List<T> datas)
            throws IllegalArgumentException, IllegalAccessException

    {
        List<Node> nodes = new ArrayList<Node>();
        Node node = null;

        for (T t : datas)
        {
            int id = -1;
            int pId = -1;
            String label = null;
            Class<? extends Object> clazz = t.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields)
            {
                if (f.getAnnotation(TreeNodeId.class) != null)
                {
                    f.setAccessible(true);
                    id = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodePid.class) != null)
                {
                    f.setAccessible(true);
                    pId = f.getInt(t);
                }
                if (f.getAnnotation(TreeNodeLabel.class) != null)
                {
                    f.setAccessible(true);
                    label = (String) f.get(t);
                }
                if (id != -1 && pId != -1 && label != null)
                {
                    break;
                }
            }
            node = new Node(id, pId, label);
            nodes.add(node);
        }

        /**
         * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
         */
        for (int i = 0; i < nodes.size(); i++)
        {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++)
            {
                Node m = nodes.get(j);
                if (m.getpId() == n.getId())
                {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId())
                {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        // 设置图片
        for (Node n : nodes)
        {
            setNodeIcon(n);
        }
        return nodes;
    }

    /**
     * 为Node设置图标
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChildren().size() > 0 && n.isExpand())
        {
            n.setIcon(R.drawable.tree_ex);
        } else if (n.getChildren().size() > 0 && !n.isExpand())
        {
            n.setIcon(R.drawable.tree_ec);
        } else
        {
            n.setIcon(-1);
        }
    }
}
